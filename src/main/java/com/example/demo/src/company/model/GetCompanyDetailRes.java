package com.example.demo.src.company.model;


import com.example.demo.src.employment.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompanyDetailRes {

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
