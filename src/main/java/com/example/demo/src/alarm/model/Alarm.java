package com.example.demo.src.alarm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Alarm {
    private int alarmIdx;
    private String type;
    private String content;
    private String date;
}
