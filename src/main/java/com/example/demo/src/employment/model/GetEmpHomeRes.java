package com.example.demo.src.employment.model;


import com.example.demo.src.company.model.GetCompanyInfoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetEmpHomeRes {

    private String recommend; // 회원일시 추천멘트

    private List<EmpBanner> empBannerUrl; // 슬라이드 배너 Url

    private List<GetEmploymentInfoRes> getEmploymentInfoResList;

    private List<GetCompanyInfoRes> getCompanyInfoResList;
}
