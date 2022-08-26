package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyEmp {

    // 1. 채용 idx
    private int employmentIdx;

    // 2. 채용 공고 명(포지션 명)
    private String employment;

    // 3. 채용 보상금
    private int compensation;

    // 4. 마감일
    private String deadline;

    // 5. 북마크 여부
    private int isBookmark;



    public CompanyEmp(int employmentIdx, String employment, int compensation, String deadline){
        this.employmentIdx = employmentIdx;
        this.employment = employment;
        this.compensation = compensation;
        this.deadline = deadline;
    }

}
