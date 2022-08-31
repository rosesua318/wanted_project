package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SettingTagSet {
    private int tagIdx;
    private String name;
    private int isSet;

    public SettingTagSet(int tagIdx, String name) {
        this.tagIdx = tagIdx;
        this.name = name;
    }
}
