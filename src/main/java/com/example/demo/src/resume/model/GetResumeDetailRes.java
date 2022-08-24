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
public class GetResumeDetailRes {

    private Resume resumeIntro; // 이력서 기본 사항들

    private List<Career> careerList; // 경력 사항들

    private List<CareerResult> careerResultList; // 성과 리스트 (경력 마다)

    private List<Education> educationList; // 학력 사항들

    private List<ResumeSkill> resumeSkillList; // 스킬 리스트

    private List<Award> awardList; // 수상 및 기타 리스트

    private List<ForeignLanguage> foreignLanguageList; // 외국어 리스트

    private List<LanguageTest> languageTestList; // 해당 외국어에 대한 시험 리스트

    private List<ResumeLink> resumeLinkList; // 링크 리스트.



}
