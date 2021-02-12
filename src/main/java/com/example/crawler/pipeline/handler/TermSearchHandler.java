package com.example.crawler.pipeline.handler;

import com.example.crawler.entity.CrawledResult;
import com.example.crawler.entity.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lvlin
 * @date 2021-02-12 14:40
 */
public final class TermSearchHandler implements Handler<CrawledResult, SearchResult> {

    private static final Logger logger = LoggerFactory.getLogger(TermSearchHandler.class);
    private final Pattern pattern;

    /**
     * determine whether a search term exists on a page
     * @param regex the search item
     */
    public TermSearchHandler(final String regex) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    }

    @Override
    public SearchResult process(final CrawledResult crawledResult) {
        Objects.requireNonNull(crawledResult);
        if (crawledResult.getStatus() != CrawledResult.STATUS_OK){
            return new SearchResult(crawledResult.getJobId(), crawledResult.getUrl(), false);
        }
        boolean result = checkExists(crawledResult.getPage().getContent());
        if (result) {
            logger.debug("Term exists on page {}", crawledResult.getJobId());
        }
        return new SearchResult(crawledResult.getJobId(), crawledResult.getUrl(), result);
    }

    boolean checkExists(final String content) {
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }
}
