package com.example.demo.src.follow.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFollowReq {

    private int userIdx; // 검증 위함

    private int companyIdx; // 팔로우하고자 하는 회사 idx


}
