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
public class GetSpecialtyRes {

    private JobGroup jobGroup;

    private List<Duty> dutyList;

    private int career;

    private List<JobSkill> jobSkillList;



}
