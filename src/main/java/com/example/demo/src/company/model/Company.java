package com.example.demo.src.company.model;

import com.example.demo.src.employment.model.Tag;
import lombok.*;

import java.util.List;

public class Company {

    @Getter
    @Builder
    @AllArgsConstructor
    public static  class Info{ // 기존 GetCompanyInfoRes

        private int companyIdx;

        private String companyImg;

        private String logoUrl;

        private String companyName;

    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private int companyIdx;
        private String name;
        private String introduce;
        private int salary;
        private int employee;
        private String companyUrl;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetailRes{

        /*  1. 맨 위에 회사로고, 회사 명 아래에 평균 연봉 , 직원 수 */
        private CompanyBasic companyBasic;

        /* 2. 채용 중인 포지션  */
        private List<CompanyEmp> companyEmpList;

        /* 3. 회사 이미지 */
        private List<CompanyImg> companyImgList;

        // 4. 뉴스 LIMIT 4
        private List<CompanyNews> companyNewsList;

        // 5. 태그
        private List<Tag> tagList;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class News{
        // 1. 뉴스 idx
        private int newsIdx;

        // 2. 뉴스 명
        private String newsTitle;

        // 3. 뉴스 생성 날짜
        private String createdAt;
    }

    @Getter
    @AllArgsConstructor
    public static class NewsRes {

        private int newsIdx;

        private String newsUrl;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Image{

        private int companyImgIdx;

        private String companyImg;
    }

}
