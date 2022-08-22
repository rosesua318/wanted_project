package com.example.demo.src.position.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetOpenPositionRes {
    private JobCategory jobCategory;
    private DutyCategory dutyCategory;
    private EmpRegion region;
    private String career; // 경력 (전체로)
    private List<SearchCategory> searchCaegories;
    private List<Company> companyList;
    private List<Employment> employmentList;
}
