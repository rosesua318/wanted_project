package com.example.demo.src.employment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpDetail {

    // 이미지는 따로 뺌

    private String employment; // 채용 포지션 명

    private String introduce; // 소개 글

    private String deadline; // 마감 일

    private String location; // 근무지역

    private String recommend; // 채용보상금 - 추천인
    private String applicant; // 채용보상금 - 지원자





}
