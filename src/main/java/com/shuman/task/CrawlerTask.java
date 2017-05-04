package com.shuman.task;

import com.shuman.interfaces.CrawlerService;
import com.shuman.interfaces.HttpService;
import com.shuman.interfaces.WordCounterService;
import com.shuman.utils.CrawlerTaskHelper;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class CrawlerTask implements Callable<Map<String, Integer>> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final CrawlerService crawlerService;

    private final HttpService httpService;

    private final WordCounterService wordCounterService;

    private final String url;

    private final int desiredScanningDepth;

    private final int currentScanningDepth;

    public CrawlerTask(CrawlerService crawlerService, CrawlerTaskHelper helper, String url, int currentScanningDepth) {
        this.crawlerService = crawlerService;
        this.httpService = helper.getHttpService();
        this.wordCounterService = helper.getWordCounterService();
        this.url = url;
        this.desiredScanningDepth = helper.getDesiredScanningDepth();
        this.currentScanningDepth = currentScanningDepth;
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        Document document;
        try {
            log.debug("making request to [{}]", url);
            document = httpService.makeRequest(url);
            log.debug("done request to [{}]", url);


            if (document == null) {
                return null;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        if (currentScanningDepth < desiredScanningDepth) {
            document.select("a[href]")
                    .forEach(el -> crawlerService.tryQueueUrl(el.attr("href"), currentScanningDepth+1));
        }

        return wordCounterService.countWords(document.text());
    }
}
