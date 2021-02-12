package com.example.crawler.io;

import com.example.crawler.entity.Job;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author lvlin
 * @date 2021-02-12 17:25
 */
public final class JobLoader {
    private final String filename;

    public JobLoader(final String filename) {
        this.filename = filename;
    }

    public Iterable<Job> load() throws IOException {
        List<Job> jobs = new LinkedList<>();
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
            if (scanner.hasNextLine()) {
                // ignore the header line
                scanner.nextLine();
            }

            while (scanner.hasNextLine()) {
                final String[] split = scanner.nextLine().split(",");
                if (split.length >= 2) {
                    String url = split[1];
                    url = url.replace("\"", "");
                    if (!url.startsWith("http")) {
                        url = "http://" + url;
                    }
                    jobs.add(Job.newJobBuilder()
                            .id(Integer.parseInt(split[0]))
                            .url(url).build());
                }
            }
            return jobs;
        }
    }
}
