package com.example.demo.src.employment.model;


import com.example.demo.src.company.model.CompanyEmpInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEmpDetailRes {

    private List<String> employmentImg;

    private CompanyEmpInfo companyEmpInfo;

    private List<String> tag;

    private EmpDetail empDetail;

    private List<String> skill;


}
