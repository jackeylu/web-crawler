package com.example.crawler.download;

import com.example.crawler.entity.CrawledResult;
import com.example.crawler.entity.Page;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lvlin
 * @date 2021-02-12 19:14
 */
public final class HttpGetDownloader{
    private static final Logger logger = LoggerFactory.getLogger(HttpGetDownloader.class);
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36";

    private final OkHttpClient client;
    private final BlockingQueue<CrawledResult> resultQueue;
    private final AtomicInteger remainRequests = new AtomicInteger(0);

    public HttpGetDownloader(final int maxRequests,
                             final Duration connectTimeoutMillis,
                             final Duration readTimeoutMillis,
                             final BlockingQueue<CrawledResult> resultQueue) {
        this.resultQueue = resultQueue;

        client = buildClient(maxRequests, connectTimeoutMillis, readTimeoutMillis);
    }

    public void submitDownloadingJob(final int id, final String url) {
        logger.info("downloading {}  {}", id, url);
        fetchHttpContent(id, url);
    }

    public boolean hasRemainRequest() {
        return remainRequests.get() > 0;
    }

    private OkHttpClient buildClient(final int maxRequests,
                                     final Duration connectTimeoutMillis,
                                     final Duration readTimeoutMillis) {
        final Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        return new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutMillis)
                .readTimeout(readTimeoutMillis)
                .dispatcher(dispatcher)
                .build();
    }

    void fetchHttpContent(final int jobId,
                          final String url) {
        Request request = new Request.Builder()
                .addHeader("User-Agent", DEFAULT_USER_AGENT)
                .url(url)
                .get()
                .build();
        remainRequests.incrementAndGet();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                logger.warn("call job {} for {} with failure {}", jobId, url, e.getMessage());
                insertFailedResult();

                remainRequests.decrementAndGet();
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                logger.info("response for {} with code {}", url, response.code());

                if (response.isSuccessful()) {
                    final ResponseBody body = response.body();
                    if (null != body) {
                        try {
                            insertSuccessResult(body.string());
                        }catch (IOException e){
                            insertFailedResult();
                            logger.warn("on retrieving body content, the job {}, url {}, failed message: {}", jobId, url, e.getMessage());
                        }
                    } else {
                        insertFailedResult();
                    }
                } else {
                    logger.warn("failed {}, {}", response.code(), response.message());
                    insertFailedResult();
                }

                response.close();
                remainRequests.decrementAndGet();
            }

            private void insertSuccessResult(final String content) {
                try {
                    resultQueue.put(new CrawledResult(CrawledResult.STATUS_OK, jobId, url, new Page(jobId, content)));
                } catch (InterruptedException e) {
                    logger.error(String.format("failed to put success result, job id %d", jobId));
                }
            }

            private void insertFailedResult() {
                try {
                    resultQueue.put(new CrawledResult(CrawledResult.STATUS_FAILED, jobId, url, null));
                } catch (InterruptedException e) {
                    logger.error(String.format("failed to put result, job id %d", jobId));
                }
            }
        });
    }
}
