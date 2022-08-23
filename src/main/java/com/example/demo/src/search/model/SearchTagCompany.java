package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SearchTagCompany {
    private int companyIdx;
    private String logoUrl;
    private String companyName;
    private int isFollow;
    private List<String> tagList;

    public SearchTagCompany(int companyIdx, String logoUrl, String companyName) {
        this.companyIdx = companyIdx;
        this.logoUrl = logoUrl;
        this.companyName = companyName;
    }
}
