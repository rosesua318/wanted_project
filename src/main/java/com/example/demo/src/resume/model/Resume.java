package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resume {


    //이력서 테이블
    private int resumeIdx; // 이력서 인덱스

    private String language; // 이력서 작성 언어

    private String title; // 이력서 명

    // 유저 테이블
    private String name; // 유저 이름

    private String email;

    private String phone;

    // 이력서 테이블
    private String introduce;
}
