package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyComment {
    private int userIdx; //댓글 작성 사용자
    private int commentIdx;
    private String comment;
    private String date; // 댓글 작성 날짜
    private int postingIdx;
    private String title; // 댓글 단 게시글 제목
    private int isLike; // 사용자가 좋아요 했는지 여부
    private int likeNum; // 좋아요 개수
    private int isComment; // 사용자가 댓글 남겼는지 여부
    private int commentNum; // 댓글 개수
}
