package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPost {
    private int postingIdx;
    private int userIdx;
    private String profileUrl;
    private String name;
    private String date;
    private String title;
    private String content;
    private String imageUrl;
    private int isLike; // 사용자가 좋아요 했는지 여부
    private int likeNum; // 좋아요 개수
    private int isComment; // 사용자가 댓글 남겼는지 여부
    private int commentNum; // 댓글 개수
}
