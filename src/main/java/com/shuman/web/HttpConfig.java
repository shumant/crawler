package com.shuman.web;

import com.shuman.utils.Params;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @Autowired
    private Params params;

    @Bean
    public UrlValidator urlVerifier() {
        String[] schemes = {"http", "https"};
        return new UrlValidator(schemes);
    }

    @Bean
    public HttpClient httpClient() {
        //todo implement warning when getting 429 error
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(5 * 1000)
                .setConnectTimeout(5 * 1000)
                .build();

        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        poolingConnManager.setMaxTotal(params.getHttpConnPerHost());
        poolingConnManager.setDefaultMaxPerRoute(params.getHttpConnPerHost());

        return HttpClients.custom()
                .setConnectionManager(poolingConnManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .setDefaultRequestConfig(config)
                .build();
    }
}
