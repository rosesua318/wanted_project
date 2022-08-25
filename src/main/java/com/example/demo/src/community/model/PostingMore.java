package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostingMore {
    private int postingIdx;
    private int userIdx;
    private String profileUrl;
    private String name;
    private String job;
    private String career;
    private String date;
    private String title;
    private String content;
    private String imageUrl;
    private List<CommunityTag> tags;
    private int isLike; // 사용자가 좋아요 했는지 여부
    private int likeNum; // 좋아요 개수
    private int isComment; // 사용자가 댓글 남겼는지 여부
    private int commentNum; // 댓글 개수

    public PostingMore(int postingIdx, int userIdx, String profileUrl, String name, String job,
                       String career, String date, String title, String content, String imageUrl,
                       int isLike, int likeNum, int isComment, int commentNum) {
        this.postingIdx = postingIdx;
        this.userIdx = userIdx;
        this.profileUrl = profileUrl;
        this.name = name;
        this.job = job;
        this.career = career;
        this.date = date;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.isLike = isLike;
        this.likeNum = likeNum;
        this.isComment = isComment;
        this.commentNum = commentNum;
    }
}
