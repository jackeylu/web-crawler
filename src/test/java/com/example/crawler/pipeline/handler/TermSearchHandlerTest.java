package com.example.crawler.pipeline.handler;

import com.example.crawler.pipeline.handler.TermSearchHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author lvlin
 * @date 2021-02-12 16:14
 */
class TermSearchHandlerTest {

    @Test
    void should_pass_regex_search() {
        TermSearchHandler termSearchHandler = new TermSearchHandler("abc\\d");
        assertFalse(termSearchHandler.checkExists("abc"));
        assertFalse(termSearchHandler.checkExists("abc_12"));
        assertTrue(termSearchHandler.checkExists("abc1"));
        assertTrue(termSearchHandler.checkExists("abc12"));
        assertTrue(termSearchHandler.checkExists("Abc1"));
        assertTrue(termSearchHandler.checkExists("abC12"));
    }

    @Test
    void should_pass_simple_text_search() {
        TermSearchHandler termSearchHandler = new TermSearchHandler("abc");
        assertTrue(termSearchHandler.checkExists("abc"));
        assertTrue(termSearchHandler.checkExists("abc_12"));
        assertTrue(termSearchHandler.checkExists("ABC_12"));
        assertFalse(termSearchHandler.checkExists("AB"));
    }
}