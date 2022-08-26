package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetProfileRes {
    private int userIdx;
    private String profileUrl;
    private String name;
    private int isNickname;
    private String nickname;
}
