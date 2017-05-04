package com.shuman.utils;

import com.shuman.interfaces.HttpService;
import com.shuman.interfaces.WordCounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrawlerTaskHelperImpl implements CrawlerTaskHelper {
    @Autowired
    private UrlVerifier urlVerifier;

    @Autowired
    private HttpService httpService;

    @Autowired
    private WordCounterService wordCounterService;

    private int desiredScanningDepth;

    @Override
    public UrlVerifier getUrlVerifier() {
        return urlVerifier;
    }

    @Override
    public HttpService getHttpService() {
        return httpService;
    }

    @Override
    public WordCounterService getWordCounterService() {
        return wordCounterService;
    }

    @Override
    public int getDesiredScanningDepth() {
        return desiredScanningDepth;
    }

    @Override
    public void setDesiredScanningDepth(int desiredScanningDepth) {
        this.desiredScanningDepth = desiredScanningDepth;
    }
}
