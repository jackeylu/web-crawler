package com.example.crawler.pipeline.handler;

/**
 * @author lvlin
 * @date 2021-02-12 14:37
 */
public interface Handler<I, O> {
    /**
     * handle the given input, and output something in the type of O
     * @param input input
     * @return processed result
     */
    O process(I input);
}
