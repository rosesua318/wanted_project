package com.example.demo.src.wanted.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetWantedRes {
    private User user;
    private List<InterestTag> interestTags;
    private int point;
    private int interested;
    private int browse;
    private int proposal;
    private ApplicationStatus applicationStatus;
    private Profile profile;
    private List<Employment> bookmarks;
    private List<Employment> likeEmps;
}
