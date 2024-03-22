package com.mystudy.lucenetest.service;

import com.mystudy.lucenetest.dto.WebContentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class WebCrawlingService {

    private final IndexService indexService;

    private String regExp = "\\<span.*\\)</span>";

    public void webCrawlingByJsoup(String url, String cssQuery) {
        List<WebContentDto> list = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select(cssQuery);

            for (Element element : elements) {
                String title = element.html().replaceAll(regExp, "");
                String hrefUrl = element.attr("href").toString();
                WebContentDto webContentDto = new WebContentDto(hrefUrl, title);
                list.add(webContentDto);
            }

        } catch (IOException e) {
            log.info("error : ", e);
        }
        indexService.doIndex(list);
    }
}
