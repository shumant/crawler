package com.shuman.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.shuman.interfaces.HttpService;
import com.shuman.utils.Params;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Service
public class HttpServiceImpl implements HttpService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private Params params;

    private RateLimiter limiter;

    @PostConstruct
    public void init() {
        limiter = RateLimiter.create(params.getRequestsPerSecond());
    }

    @Override
    public Document makeRequest(String validUrl) throws IOException {
        limiter.acquire();
        InputStream in = null;

        try {
            HttpUriRequest get = new HttpGet(validUrl);
            HttpEntity entity = httpClient.execute(get).getEntity();
            in = entity.getContent();
            Header encoding = entity.getContentEncoding();

            return Jsoup.parse(in, encoding == null ? "UTF-8" : encoding.getValue(), "");
        } catch (IllegalArgumentException | IOException e) {
            //todo handle empty response and http request exception
            return null;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
