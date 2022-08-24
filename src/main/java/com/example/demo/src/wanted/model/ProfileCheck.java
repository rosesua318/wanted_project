package com.example.demo.src.wanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileCheck {
    private int isResume;
    private int isSpecialty;
    private int isEducation;
    private int isCareer;
    private int isIntroduce;
    private String introduce;
}
