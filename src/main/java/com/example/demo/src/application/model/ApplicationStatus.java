package com.example.demo.src.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationStatus {
    private int total;
    private int completed;
    private int browse;
    private int pass;
    private int fail;
}
