package com.example.crawler.entity;

/**
 * A page is an collection of the content of a web page.
 *
 * @author lvlin
 * @date 2021-02-12 9:50
 */
public final class Page {
    private final String content;
    private final int jobId;

    public Page(final int jobId, final String content) {
        this.jobId = jobId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content='" + content + '\'' +
                '}';
    }

    public int getJobId() {
        return jobId;
    }
}
