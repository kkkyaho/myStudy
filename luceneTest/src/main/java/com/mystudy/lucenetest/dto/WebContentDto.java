package com.mystudy.lucenetest.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WebContentDto {

    private String url;
    private String title;
    private String htmlCode;
    private String content;
    private LocalDateTime date;




    public WebContentDto() {
    }

    public WebContentDto(String url, String title, String htmlCode, String content) {
        this.url = url;
        this.title = title;
        this.htmlCode = htmlCode;
        this.content = content;
    }

    @Override
    public String toString() {
        return "WebContentDto{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", htmlCode='" + htmlCode + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
