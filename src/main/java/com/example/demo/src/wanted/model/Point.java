package com.example.demo.src.wanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Point {
    private int isPlus;
    private int cost;
}
