package com.shuman.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrawlerServiceImplTest {
    @InjectMocks
    private CrawlerServiceImpl crawlerService;

    @Test
    @Ignore
    public void runCrawler() throws Exception {
        //todo implement with approx count of words
        crawlerService.runCrawler("yandex.ru", 5);
    }

}