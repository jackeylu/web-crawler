package com.example.crawler.entity;

/**
 * @author lvlin
 * @date 2021-02-12 15:25
 */
public final class Job {
    private final int id;
    private final String url;

    public Job(final int id, final String url) {
        this.id = id;
        this.url = url;
    }

    public static JobBuilder newJobBuilder() {
        return new JobBuilder();
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }

    public static class JobBuilder{
        private int id;
        private String url;

        public JobBuilder id(final int id) {
            this.id = id;
            return this;
        }

        public JobBuilder url(final String url) {
            this.url = url;
            return this;
        }

        public Job build() {
            return new Job(id, url);
        }
    }
}
