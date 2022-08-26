package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikeRes {
    private int employmentIdx;
    private String employmentImg;
    private String employment;
    private String companyName;
    private String nation;
    private String region;
    private int reward;

    public GetLikeRes(int employmentIdx, String employment, String companyName) {
        this.employmentIdx = employmentIdx;
        this.employment = employment;
        this.companyName = companyName;
    }
}
