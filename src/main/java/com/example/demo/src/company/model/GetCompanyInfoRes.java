package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompanyInfoRes {

    private int companyIdx;

    private String companyImg;

    private String logoUrl;

    private String companyName;

    //private String category; 보류

}
