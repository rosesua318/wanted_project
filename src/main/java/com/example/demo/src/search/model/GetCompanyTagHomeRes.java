package com.example.demo.src.search.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCompanyTagHomeRes {
    private List<SearchTag> recommendTags;
    private List<SearchTagCompany> companies;
}
