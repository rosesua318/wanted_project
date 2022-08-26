package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyUser {
    private int myUserIdx;
    private String myProfileUrl;
    private String myName;
    private String myJob;
    private String myCareer;
}
