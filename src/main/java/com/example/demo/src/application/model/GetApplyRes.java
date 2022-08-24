package com.example.demo.src.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetApplyRes {
    private int writing;
    private int apply;
    private ApplicationStatus applicationStatus;
    private List<Application> applications;
}
