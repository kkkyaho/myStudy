package com.mystudy.lucenetest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebContentDto {

    private String url;
    private String title;

    public WebContentDto() {
    }

    public WebContentDto(String url, String title) {
        this.url = url;
        this.title = title;
    }

    @Override
    public String toString() {
        return "WebContentDto{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
