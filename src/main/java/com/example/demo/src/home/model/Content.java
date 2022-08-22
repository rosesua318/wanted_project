package com.example.demo.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Content {
    private int contentIdx;
    private String imageUrl;
    private String title;
    private String contentUrl;
    private String content;
    private String creator;
    private String creatorImg;
}
