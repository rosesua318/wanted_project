package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Award {

    private int awardIdx; // 수상 인덱스

    private String createdAt; // 수상 날짜

    private String title; // 활동 명

    private String content; // 세부사항
}
