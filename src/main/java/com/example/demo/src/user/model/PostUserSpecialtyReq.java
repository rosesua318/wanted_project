package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostUserSpecialtyReq {

    int userIdx;

    private int jobGroupIdx;

    private int dutyIdx; // 테이블 설계 구조상 단일값 만 받아오는 걸로 구현

    private int career;

    private List<JobSkill> jobSkillList;


}
