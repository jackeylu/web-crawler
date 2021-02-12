package com.example.crawler.entity;

/**
 * @author lvlin
 * @date 2021-02-12 15:41
 */
public final class SearchResult {

    private final int jobId;
    private final String url;
    private final boolean exists;

    public SearchResult(final int jobId, final String url, final boolean exists) {
        this.jobId = jobId;
        this.url = url;
        this.exists = exists;
    }

    public int getJobId() {
        return jobId;
    }

    public boolean isExists() {
        return exists;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "jobId=" + jobId +
                ", url='" + url + '\'' +
                ", exists=" + exists +
                '}';
    }
}
