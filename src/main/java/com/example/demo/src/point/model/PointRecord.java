package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PointRecord {
    private int pointIdx;
    private String date;
    private String title;
    private String isPlus;
    private int cost;
    private String endAt;
}
