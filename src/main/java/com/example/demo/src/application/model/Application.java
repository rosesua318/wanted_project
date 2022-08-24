package com.example.demo.src.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Application {
    private int applicationIdx;
    private String companyName;
    private String position;
    private String writeTime;
    private String state; // 0=작성 중, 1=지원 완료, 2=이력서 열람, 3=최종합격, 4=불합격
    private String recommend;
}
