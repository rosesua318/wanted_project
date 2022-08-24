package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareerResult {


    private int careerIdx; // 해당 경력 인덱스
    // 성과 테이블 값

    private int resultIdx; // 성과 Idx

    private String title; // 주요 성과

    private String startedAt; // 성과 시작일

    private String endAt; // 성과 마감일 (여기는 마감일 필수)

    private String content; // 상세 업무내용과 성과

}
