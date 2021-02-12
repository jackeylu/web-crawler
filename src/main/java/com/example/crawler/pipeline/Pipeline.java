package com.example.crawler.pipeline;

import com.example.crawler.pipeline.handler.Handler;

/**
 * @author lvlin
 * @date 2021-02-12 14:43
 */
public final class Pipeline<I, O> {
    private final Handler<I, O> currentHandler;

    public Pipeline(final Handler<I, O> currentHandler) {
        this.currentHandler = currentHandler;
    }

    public <K> Pipeline<I, K> addHandler(Handler<O, K> newHandler) {
        return new Pipeline<>(input -> newHandler.process(currentHandler.process(input)));
    }

    public O execute(I input) {
        return currentHandler.process(input);
    }
}
