package com.shuman;

import com.shuman.interfaces.CrawlerEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CrawlerRunner implements CommandLineRunner {
    private static final String PROVIDE_ARGS_ERROR = "Please provide URL and scanning depth. Example: 'java -jar " +
            "<path-to-jar> <url> <scanning depth>'";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CrawlerEntryPoint crawler;

    @Override
    public void run(String... strings) throws Exception {
        if (strings.length < 2) {
            log.error(PROVIDE_ARGS_ERROR);
        } else {
            int desiredScanningDepth = 0;

            try {
                desiredScanningDepth = Integer.parseInt(strings[1]);
                crawler.runCrawler(strings[0], desiredScanningDepth);
            } catch (NumberFormatException ex) {
                log.error("{}\n{}", PROVIDE_ARGS_ERROR, ex.getMessage());
                System.exit(1);
            } catch (RuntimeException ex) {
                log.error("RunTime exception! Message: " + ex.getMessage(), ex.getCause());
                System.exit(1);
            }

        }
    }
}
