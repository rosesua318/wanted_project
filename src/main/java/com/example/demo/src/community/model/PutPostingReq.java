package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PutPostingReq {
    private int postingIdx;
    private List<Integer> tags; // 무조건 1개 이상이어야 함
    private String title; // 비어있으면 안됨
    private String content; // 비어있으면 안됨
}
