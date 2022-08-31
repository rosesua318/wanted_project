package com.example.demo.src.employment.model;

import com.example.demo.src.company.model.CompanyEmpInfo;
import com.example.demo.src.company.model.GetCompanyInfoRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class Employment {



    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRes {

        private List<Employment.Image> employmentImg;

        private CompanyEmpInfo companyEmpInfo;

        private List<Tag> tag;

        private EmpDetail empDetail;

        private List<Skill> skill;


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeRes {

        private String recommend; // 회원일시 추천멘트

        private List<EmpBanner> empBannerUrl; // 슬라이드 배너 Url

        private List<GetEmploymentInfoRes> getEmploymentInfoResList;

        private List<GetCompanyInfoRes> getCompanyInfoResList;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Image {

        private int employmentIdx;
        private int employmentImgIdx;

        private String employmentImg;
    }



}
