package com.example.demo.src.company.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCompanyNewsRes {

    private int newsIdx;

    private String newsUrl;
}
