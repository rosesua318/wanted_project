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
public class ForeignLanguage {

    // 외국어 테이블
    private int foreignLngIdx; // 외국어 인덱스

    private String language; // 언어

    private int level; // 수준 (유창함,비즈니스 등)





}
