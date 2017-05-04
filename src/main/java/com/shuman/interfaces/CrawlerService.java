package com.shuman.interfaces;

public interface CrawlerService {
    void tryQueueUrl(String url, int currentScanningDepth);
}
