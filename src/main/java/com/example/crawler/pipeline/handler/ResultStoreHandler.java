package com.example.crawler.pipeline.handler;

import com.example.crawler.entity.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author lvlin
 * @date 2021-02-12 21:28
 */
public final class ResultStoreHandler implements Handler<SearchResult, Boolean>, AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(ResultStoreHandler.class);
    private final BufferedWriter writer;

    public ResultStoreHandler(final String filename) throws IOException {
        writer = Files.newBufferedWriter(Paths.get(filename),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        writer.write("\"Rank\",\"URL\",\"Term Exists\"\n");
    }

    @Override
    public Boolean process(final SearchResult result) {
        logger.debug("processing {}", result);
        try {
            writer.write(String.format("%d,\"%s\",%d%n",
                    result.getJobId(), result.getUrl(), result.isExists() ? 1 : 0));
            return true;
        } catch (IOException e) {
            logger.error(String.format("Failed to write down result %s", result), e);
            throw new RuntimeException("Unable to handle this exception.", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }
}
