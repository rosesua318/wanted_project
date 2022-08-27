package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSkill {


    private int skillIdx;

    private String skill;

    public JobSkill(int skillIdx){
        this.skillIdx = skillIdx;
    }
}
