package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyNews {

    // 1. 뉴스 idx
    private int newsIdx;

    // 2. 뉴스 명
    private String newsTitle;

    // 3. 뉴스 생성 날짜

    private String createdAt;
}
