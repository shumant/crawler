package com.shuman.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Params {
    @Value("${crawlerThreads}")
    private String crawlerThreads;

    @Value("${http.connPerHost}")
    private String httpConnPerHost;

    @Value("${http.requestsPerSecond}")
    private String requestsPerSecond;

    public int getCrawlerThreads() {
        return Integer.parseInt(crawlerThreads);
    }

    public int getHttpConnPerHost() {
        return Integer.parseInt(httpConnPerHost);
    }

    public int getRequestsPerSecond() {
        return Integer.parseInt(requestsPerSecond);
    }
}
