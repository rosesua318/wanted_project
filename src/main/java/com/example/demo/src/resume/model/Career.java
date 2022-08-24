package com.example.demo.src.resume.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Career {

    // 경력 테이블 값
    private int careerIdx; // 경력 인덱스

    private String company; // 회사,직장 명

    private String department; // 부서명 직책

    private int isPresent; //현재 재직 중 여부

    private String startedAt; // 경력 시작일

    private String endAt; // 경력 마감일(isPresent가 1이라면 null 반환 예정)






}
