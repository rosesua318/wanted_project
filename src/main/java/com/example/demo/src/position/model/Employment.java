package com.example.demo.src.position.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Employment {
    private int employmentIdx;
    private String thumbnail;
    private String title;
    private String companyName;
    private String region;
    private String nation;
    private String reward;
    private int isBookmark;

    public Employment(int employmentIdx, String title, String companyName, String region, String nation) {
        this.employmentIdx = employmentIdx;
        this.title = title;
        this.companyName = companyName;
        this.region = region;
        this.nation = nation;
    }

    public Employment(int employmentIdx, String title, String companyName, String region, String nation, int isBookmark) {
        this.employmentIdx = employmentIdx;
        this.title = title;
        this.companyName = companyName;
        this.region = region;
        this.nation = nation;
        this.isBookmark = isBookmark;
    }
}
