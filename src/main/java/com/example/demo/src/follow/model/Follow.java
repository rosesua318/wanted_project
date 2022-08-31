package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Follow {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostReq {

        private int userIdx; // 검증 위함

        private int companyIdx; // 팔로우하고자 하는 회사 idx
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostRes {
        private int followIdx;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchStatusReq {

        private int companyIdx;
    }
}
