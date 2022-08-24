package com.example.demo.src.wanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TagSet {
    private int itIdx;
    private String wideTag;
    private List<InterestTagSet> tags;

    public TagSet(int itIdx, String wideTag) {
        this.itIdx = itIdx;
        this.wideTag = wideTag;
    }
}
