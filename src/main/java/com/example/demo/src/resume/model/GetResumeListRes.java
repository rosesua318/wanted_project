package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GetResumeListRes {


    private int resumeIdx; // 이력서 인덱스

    private String title;; // 이력서 명

    private String updatedAt; //  최종 수정 날짜


    private String status; // active 면 작성 완료 이런 식으로 구현 예정

    private String language; // 작성된 언어

}
