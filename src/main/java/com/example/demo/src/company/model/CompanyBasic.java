package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBasic {

    // 1. 회사 로고

    private String logoUrl;

    // 2. 회사 명
    private String companyName;

    // 3. 평균 연봉
    private String salary;

    // 4. 직원 수
    private String employee; // 비회원시에 출력 문구를 위해 String 선언.

    // 5. 회사 팔로우 여부
    private int isFollow;


    // 비 회원용 전용  Constructor
    public CompanyBasic(String logoUrl,String companyName){
        this.logoUrl = logoUrl;
        this.companyName = companyName;
        salary = "이 회사의 연봉과 인원을 보고 싶다면?";
        employee = "이 회사의 연봉과 인원을 보고 싶다면?";
    }
}
