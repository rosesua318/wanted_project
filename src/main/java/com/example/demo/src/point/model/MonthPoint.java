package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MonthPoint {
    private String month;
    private List<PointRecord> points;

    public MonthPoint(String month) {
        this.month = month;
    }
}
