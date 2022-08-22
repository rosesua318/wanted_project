package com.example.demo.src.home.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetHomeRes {
    private List<String> bannerList;
    private List<HomeCategory> categoryList;
    private List<Content> contents;
}