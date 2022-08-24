package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    private int eduIdx; // 학력 인덱스

    private String university; // 학교 명

    private String major; // 전공 명

    private String study; // 이수과목 또는 연구 내용

    private int isPresent; // 현재 재학 중 여부

    private String startedAt; // 학력 시작일

    private String endAt; // 학력 마감일(재학 중이 아니라면 null)

}
