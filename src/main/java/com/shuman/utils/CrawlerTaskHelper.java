package com.shuman.utils;

import com.shuman.interfaces.HttpService;
import com.shuman.interfaces.WordCounterService;

public interface CrawlerTaskHelper {
    UrlVerifier getUrlVerifier();

    HttpService getHttpService();

    WordCounterService getWordCounterService();

    int getDesiredScanningDepth();

    void setDesiredScanningDepth(int desiredScanningDepth);
}
