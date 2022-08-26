package com.example.demo.src.employment.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostApplicationReq {

    private int userIdx;
    private int employmentIdx;

    private String name;
    private String email;
    private String phone;
    private int resumeIdx;
}
