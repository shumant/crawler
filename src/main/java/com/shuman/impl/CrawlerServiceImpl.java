package com.shuman.impl;

import com.shuman.interfaces.CrawlerEntryPoint;
import com.shuman.interfaces.CrawlerService;
import com.shuman.task.CrawlerTask;
import com.shuman.utils.CrawlerTaskHelper;
import com.shuman.utils.Params;
import com.shuman.utils.UrlVerifier;
import com.shuman.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CrawlerServiceImpl implements CrawlerService, CrawlerEntryPoint {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private Params params;

    @Autowired
    private CrawlerTaskHelper crawlerTaskHelper;

    @Autowired
    private UrlVerifier urlVerifier;

    private CompletionService<Map<String, Integer>> executorService;

    private ThreadPoolExecutor executor;

    private AtomicInteger activeTasksCount = new AtomicInteger(0);

    private Map<String, Integer> finalWords = new ConcurrentHashMap<>(1000, 0.75f, 30);

    @PostConstruct
    private void init() {
        executor = new ThreadPoolExecutor(params.getCrawlerThreads(), params.getCrawlerThreads(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        executorService = new ExecutorCompletionService<>(executor);
    }

    @Override
    public void tryQueueUrl(String url, int nextScanningDepth) {
        url = urlVerifier.normalizeVerifyAndAddToSeen(url);

        if (url != null) {
            log.debug("Adding url [{}] with scanningDepth [{}] to queue", url, nextScanningDepth);

            activeTasksCount.incrementAndGet();
            executorService.submit(new CrawlerTask(
                    this,
                    crawlerTaskHelper,
                    url,
                    nextScanningDepth));
        }

    }

    @Override
    public void runCrawler(String url, int desiredScanningDepth) {
        urlVerifier.setRootHostName(url);

        crawlerTaskHelper.setDesiredScanningDepth(desiredScanningDepth);
        tryQueueUrl(url, 1);

        Thread monitoringThread = new Thread(new MonitoringTask());
        monitoringThread.start();

    }

    private void updateWordsCount(Map<String, Integer> words) {
        if (words == null) return;
        words.forEach((key, value) -> finalWords.merge(key, value, Utils::AddInts));
    }

    private class MonitoringTask implements Runnable {
        @Override
        public void run() {
            while (activeTasksCount.get() > 0) {
                try {
                    updateWordsCount(executorService.take().get());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    activeTasksCount.decrementAndGet();
                }
            }

            executor.shutdown();

            Map<String, Integer> result = finalWords.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                    .limit(100)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            log.info("Crawling completed!");
            result.forEach((key, value) -> {
                System.out.println(String.format("%s - %d раз(а)", key, value));
            });

        }
    }

}
