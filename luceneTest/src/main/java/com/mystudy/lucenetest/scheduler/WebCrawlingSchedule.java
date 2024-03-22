package com.mystudy.lucenetest.scheduler;

import com.mystudy.lucenetest.service.WebCrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class WebCrawlingSchedule {

    private final WebCrawlingService webCrawlingService;

    @Scheduled(fixedRate = 60000)
    public void runSchedule() {

        for (int i=1; i<11; i++) {
            String url = "https://bbs.ruliweb.com/best/all/now?orderby=best_id&range=24h&m=all&t=now&";
            String param = "page=" + i;
            String cssQuery = ".article_wrapper .title_wrapper";

            log.info("param   ::   " + url+param);

            webCrawlingService.webCrawlingByJsoup(url+param, cssQuery);
        }


    }
}
