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

    private List<EmploymentImg> employmentImg;

    private CompanyEmpInfo companyEmpInfo;

    private List<Tag> tag;

    private EmpDetail empDetail;

    private List<Skill> skill;


}
