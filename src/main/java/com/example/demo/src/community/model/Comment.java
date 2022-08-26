package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private int commentIdx;
    private int userIdx;
    private String profileUrl;
    private String name;
    private String date;
    private String content;
}
