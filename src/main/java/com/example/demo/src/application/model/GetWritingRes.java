package com.example.demo.src.application.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWritingRes {
    private int writing;
    private int apply;
    private List<Application> applications;
}
