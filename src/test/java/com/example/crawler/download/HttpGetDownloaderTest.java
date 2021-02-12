package com.example.crawler.download;

import com.example.crawler.entity.CrawledResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lvlin
 * @date 2021-02-12 20:20
 */
class HttpGetDownloaderTest {
    private static final Logger logger = LoggerFactory.getLogger(HttpGetDownloaderTest.class);

    @Timeout(value = 5)
    @Test
    void should_get_content() throws InterruptedException {
        final BlockingQueue<CrawledResult> queue = new LinkedBlockingQueue<>();
        final HttpGetDownloader downloader = new HttpGetDownloader(4,
                Duration.ofSeconds(1),
                Duration.ofSeconds(1),
                queue);

        downloader.submitDownloadingJob(1, "https://www.baidu.com");
        downloader.submitDownloadingJob(2, "https://www.qq.com");
        downloader.submitDownloadingJob(3, "https://www.sina.com");
        downloader.submitDownloadingJob(4, "https://www.unknownhost.abc");

        long start = System.currentTimeMillis();

        while (downloader.hasRemainRequest()) {
            CrawledResult poll = queue.take();
            logger.info("job {}, status {}", poll.getJobId(), poll.getStatus());
            if (poll.getJobId() != 4) {
                assertEquals(CrawledResult.STATUS_OK, poll.getStatus());
                assertTrue(poll.getPage().getContent().length() > 100);
            } else {
                assertEquals(CrawledResult.STATUS_FAILED, poll.getStatus());
                assertNull(poll.getPage());
            }
        }
        long cost = System.currentTimeMillis() - start;
        logger.info("Cost {} ms", cost);
    }
}