package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMyLikeRes {
    private int userIdx;
    private String profileUrl;
    private String name;
    private String type; // 닉네임, 기본
    private String job;
    private String career;

    private List<MyPost> postList;

    public GetMyLikeRes(int userIdx, String profileUrl, String name, String type, String job, String career) {
        this.userIdx = userIdx;
        this.profileUrl = profileUrl;
        this.name = name;
        this.type = type;
        this.job = job;
        this.career = career;
    }
}
