package com.example.crawler;

import com.example.crawler.config.JobConfig;
import com.example.crawler.entity.Job;
import com.example.crawler.io.JobLoader;
import com.example.crawler.worker.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lvlin
 * @date 2021-02-12 9:52
 */
public final class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        if (args.length == 0 || "--help".equals(args[0])) {
            usage();
            System.exit(1);
        }
        JobConfig config = buildJobConfig(args);
        long start = System.currentTimeMillis();
        Crawler crawler = new Crawler(config);
        final Iterable<Job> jobs = new JobLoader(config.getInput()).load();
        crawler.start(jobs);
        long cost = System.currentTimeMillis() - start;
        logger.info("Jobs done with {} ms.", cost);
        System.exit(0);
    }

    private static JobConfig buildJobConfig(final String[] args) {
        JobConfig config;
        config= new JobConfig(args[0]);
        if (args.length == 2){
            config=new JobConfig(args[0], Integer.parseInt(args[1]));
        }
        if (args.length == 3){
            config = new JobConfig(args[0], Integer.parseInt(args[1]), args[2]);
        }
        if (args.length >= 4) {
            config = new JobConfig(args[0], Integer.parseInt(args[1]), args[2], args[3]);
        }
        return config;
    }

    private static void usage() {
        System.err.println("web crawler.");
        System.err.println("<term> [maxRequest] [input] [output]");
        System.err.println("term\t\tthe search term");
        System.err.println("maxRequest\tthe max number of concurrent requests");
        System.err.println("input\t\tthe urls to be search, default is web_crawler_url_list.txt");
        System.err.println("output\t\tthe output filename, default is result.txt");
    }
}
