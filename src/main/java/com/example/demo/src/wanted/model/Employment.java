package com.example.demo.src.wanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Employment {
    private int employmentIdx;
    private String logoUrl;
    private String title;
    private String companyName;
    private String region;
    private String nation;
}
