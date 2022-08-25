package com.example.demo.src.resume.model;


import com.example.demo.src.wanted.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageTest {



    // 어학시험 테이블
    private int foreignLngIdx; // 해당하는 외국어 인덱스

    private int languageTestIdx; // 어학시험 인덱스(Test 테이블 메인키)
    private String title; // 어학시험 명

    private String score; // 어학시험 점수

    private String createdAt; // 어학시험 날짜


}
