package com.example.crawler.config;

import java.time.Duration;
import java.util.Objects;

/**
 * @author lvlin
 * @date 2021-02-12 16:35
 */
public final class JobConfig {

    private static final int DEFAULT_MAX_REQUEST = 20;
    private static final String DEFAULT_INPUT = "web_crawler_url_list.txt";
    private static final String DEFAULT_OUTPUT = "result.txt";
    private static final int DEFAULT_CONNECTION_TIMEOUT = 2000;
    private static final int DEFAULT_READ_TIMEOUT = 10000;
    /**
     * MAX requests at any given time, default is 20.
     */
    private int maxRequests;
    /**
     * The term to be search in the page
     */
    private String term;
    /**
     * a file include the ranks and urls to be crawled
     */
    private String input;
    /**
     * the filename to store the search result.
     */
    private String output;

    public JobConfig(final String term, final int maxRequests, final String input, final String output){
        init(term, maxRequests, input, output);
    }

    private void init(final String term, final int maxRequests, final String input, final String output){
        if (maxRequests <= 0) {
            throw new IllegalArgumentException("maxRequest");
        }
        this.maxRequests = maxRequests;

        this.term = stringChecking(term, "term");
        this.input = stringChecking(input, "input filename");
        this.output = stringChecking(output, "output filename");
    }

    public int getMaxRequests() {
        return maxRequests;
    }

    public String getTerm() {
        return term;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }


    private String stringChecking(final String value, final String name) {
        Objects.requireNonNull(name);
        if (value == null){
            throw new IllegalArgumentException(String.format("%s is null", name));
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException(String.format("%s is empty", name));
        }
        return value;
    }

    public Duration getConnectTimeout() {
        return Duration.ofMillis(DEFAULT_CONNECTION_TIMEOUT);
    }

    public Duration getReadTimeout() {
        return Duration.ofMillis(DEFAULT_READ_TIMEOUT);
    }

    public static class JobConfigBuilder {
        private String term;
        private int maxRequests = DEFAULT_MAX_REQUEST;
        private String input = DEFAULT_INPUT;
        private String output = DEFAULT_OUTPUT;

        public JobConfigBuilder setTerm(final String term) {
            this.term = term;
            return this;
        }

        public JobConfigBuilder setMaxRequests(final int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public JobConfigBuilder setInput(final String input) {
            this.input = input;
            return this;
        }

        public JobConfigBuilder setOutput(final String output) {
            this.output = output;
            return this;
        }

        public JobConfig build() {
            return new JobConfig(term, maxRequests, input, output);
        }
    }
}
