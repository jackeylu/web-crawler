package com.example.crawler.entity;

/**
 * @author lvlin
 * @date 2021-02-12 15:15
 */
public final class CrawledResult {

    public static final int STATUS_OK = 0;
    public static final int STATUS_FAILED = -1;

    /**
     * crawled result for a page
     */
    private final int status;
    /**
     * job id for this job
     */
    private final int jobId;

    /**
     * url for this job
     */
    private final String url;
    /**
     * crawled page
     */
    private final Page page;

    public CrawledResult(final int status, final int jobId, final String url, final Page page) {
        this.status = status;
        this.jobId = jobId;
        this.url = url;
        this.page = page;
    }

    public int getStatus() {
        return status;
    }

    public Page getPage() {
        return page;
    }

    public int getJobId() {
        return jobId;
    }

    @Override
    public String toString() {
        return "CrawledResult{" +
                "status=" + status +
                ", jobId=" + jobId +
                ", url=" + url +
                ", page=" + page +
                '}';
    }

    public String getUrl() {
        return url;
    }
}
