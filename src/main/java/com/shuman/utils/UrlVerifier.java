package com.shuman.utils;

public interface UrlVerifier {
    //returns null if url is rejected
    String normalizeVerifyAndAddToSeen(String url);
    boolean isValid(String url);
    void setRootHostName(String url);
}
