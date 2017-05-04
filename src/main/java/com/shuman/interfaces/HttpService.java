package com.shuman.interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface HttpService {
    Document makeRequest(String url) throws IOException;
}
