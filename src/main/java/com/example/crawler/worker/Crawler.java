package com.example.crawler.worker;

import com.example.crawler.config.JobConfig;
import com.example.crawler.download.HttpGetDownloader;
import com.example.crawler.entity.CrawledResult;
import com.example.crawler.entity.Job;
import com.example.crawler.pipeline.Pipeline;
import com.example.crawler.pipeline.handler.ResultStoreHandler;
import com.example.crawler.pipeline.handler.TermSearchHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author lvlin
 * @date 2021-02-12 16:34
 */
public final class Crawler {
    private static final Logger logger = LoggerFactory.getLogger(Crawler.class);
    private final JobConfig jobConfig;

    public Crawler(JobConfig jobConfig) {
        this.jobConfig = jobConfig;
    }

    public void start(Iterable<Job> jobs) throws Exception {
        Objects.requireNonNull(jobs);

        final BlockingQueue<CrawledResult> queue = new LinkedBlockingQueue<>();
        final HttpGetDownloader downloader = new HttpGetDownloader(jobConfig.getMaxRequests(),
                jobConfig.getConnectTimeout(),
                jobConfig.getReadTimeout(),
                queue);

        for (final Job job : jobs) {
            downloader.submitDownloadingJob(job.getId(), job.getUrl());
        }
        logger.info("Jobs are submitted.");

        try (final ResultStoreHandler storeHandler = new ResultStoreHandler(jobConfig.getOutput())) {
            Pipeline<CrawledResult, Boolean> pipeline = new Pipeline<>(new TermSearchHandler(jobConfig.getTerm()))
                    .addHandler(storeHandler);

            while (downloader.hasRemainRequest()) {
                final CrawledResult crawledResult = queue.take();
                pipeline.execute(crawledResult);
            }
        }
        logger.info("Jobs are done.");
    }
}
