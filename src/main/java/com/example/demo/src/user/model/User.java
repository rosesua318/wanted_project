package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


public class User {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Info {
        private int userIdx;
        private String email;
        private String password;
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PostResponse{
        private String jwt;
        private int userIdx;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetRes {
        private int userIdx;
        private String name;
        private String imgUrl;
        private String email;
        private String phone;


    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{

        private String email;
        private String password;
        private String name;
        private String phone;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginRes {

        private int userIdx;
        private String jwt;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginReq {
        private String email;
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginEmailReq {

        private String email;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status{
        private int userIdx;
        private String status;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusReq {

        private int userIdx;
        private String status;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PwdModify {

        private String password;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchPwdReq {

        private int userIdx;
        private String password;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SpecialtyReq {

        int userIdx;
        private int jobGroupIdx;
        private int dutyIdx; // 테이블 설계 구조상 단일값 만 받아오는 걸로 구현
        private int career;
        private List<JobSkill> jobSkillList;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchReq {
        private int userIdx;
        private String email;
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchPrivate {

        private int userIdx;
        private int isPrivate;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PatchImage {

        private int userIdx; // 검증 위해 사용
        private String imageUrl;
    }




}
