package com.example.demo.src.resume.model;


import lombok.Getter;

@Getter
public enum ResumeTable {

    // 코드 중복을 피하기 위한 enum 선언 (ResumeDao부분)

    // 해당 테이블 명 , 해당 인덱스 , 필요한 외래 인덱스 구조로 구성.
    Career("Career","careerIdx","resumeIdx"),
    CareerResult("Result","resultIdx","careerIdx"),
    Education("Education","educationIdx","resumeIdx"),
  //  ResumeSkill("ResumeSkill","rsIdx","resumeIdx"), // 얘는 스킬 인덱스도 필요하여 구현 현재는 제외
    Award("Awards","awardsIdx","resumeIdx"),
    Language("ForeignLanguage","flIdx","resumeIdx"),
    LanguageTest("Test","testIdx","flIdx"),
    Link("Link","linkIdx","resumeIdx");

    final private String tableName;
    final private String primaryKey;
    final private String parentKey;

    ResumeTable(String tableName, String primaryKey, String parentKey) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.parentKey = parentKey;
    }



}
