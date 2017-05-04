package com.shuman.utils;

import com.google.common.net.InternetDomainName;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class UrlVerifierImpl implements UrlVerifier {
    @Autowired
    private UrlValidator urlValidator;

    //need to establish happens-before betwee contains() and add() =>
    // thread-unsafe collection for using inside synchronized
    private Set<String> seenUrl = new HashSet<>();
    private final Object seenLock = new Object();

    private String rootHostName = "";


    private static final Pattern SCHEME = Pattern.compile("http:\\/\\/|https:\\/\\/|www\\.");
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String normalizeVerifyAndAddToSeen(String url) {
        if (url.length() > 2 && url.charAt(0) == '#') {
            return null;
        }

        url = getCommonPath(url);

        if (!isValid(url)) {
            log.debug("Refused url [{}]", url);
            return null;
        }

        String shortUrl = discardScheme(url);

        synchronized (seenLock) {
            if (seenUrl.contains(shortUrl)) {
                return null;
            }

            seenUrl.add(shortUrl);
            return url;
        }
    }

    @Override
    public boolean isValid(String url) {
        String host = getTopDomain(url);

        return host.equals(rootHostName) && urlValidator.isValid(url);
    }

    @Override
    public void setRootHostName(String url) {
        url = getCommonPath(url);

        if (!urlValidator.isValid(url)) {
            throw new RuntimeException("Provided URL is not valid: " + url);
        }

        rootHostName = getTopDomain(url);
    }

    private String getTopDomain(String url) {
        try {
            URI uri = new URI(url);
            String hostName = uri.getHost();

            if (hostName == null) {
                return "";
            }
            return InternetDomainName.from(hostName).topPrivateDomain().toString();
        } catch (URISyntaxException | IllegalStateException | IllegalArgumentException e) {
            //todo sometimes InternetDomainName throws 'Not under a public suffix'
            return "";
        }
    }

    private String getCommonPath (String url) {
        if (url.length() < 2) {
            return "";
        }

        StringBuilder builder = new StringBuilder(url);

        if (builder.charAt(0) == '/') {
            if (builder.charAt(1) == '/') {
                builder.replace(0,2,"http://");
            } else {
                builder.replace(0,0,"http://" + rootHostName);
            }
        }

        url = builder.toString();

        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        try {
            URI uri = new URI(url);
            return uri.getScheme() + "://" + uri.getHost() + uri.getPath();
        } catch (URISyntaxException e) {
            return "";
        }
    }

    private String discardScheme (String url) {
        return SCHEME.matcher(url).replaceAll("");
    }
}
