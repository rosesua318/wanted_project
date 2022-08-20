package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompanyRes {

    private int companyIdx;
    private String name;
    private String introduce;
    private int salary;
    private int employee;
    private String companyUrl;
}
