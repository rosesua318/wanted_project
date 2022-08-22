package com.example.demo.src.position.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Company {
    private int companyIdx;
    private String thumbnail;
    private String logo;
    private String companyName;
    private int position;

    public Company(int companyIdx, String logo, String companyName) {
        this.companyIdx = companyIdx;
        this.logo = logo;
        this.companyName = companyName;
    }
}
