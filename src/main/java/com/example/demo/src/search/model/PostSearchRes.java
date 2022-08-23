package com.example.demo.src.search.model;

import com.example.demo.src.position.model.EmpRegion;
import com.example.demo.src.position.model.Employment;
import com.example.demo.src.position.model.SearchCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostSearchRes {
    private String searchKeyword;
    private List<RelatedDuty> relatedDuties;
    private List<SearchTagCompany> companies;
    private EmpRegion region;
    private String career; // 경력 (전체로)
    private List<SearchCategory> searchCaegories;
    private List<Employment> employmentList;
}
