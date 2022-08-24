package com.example.demo.src.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetPointRes {
    private int point;
    private int destroyPoint;
    private List<MonthPoint> monthPoints;
}
