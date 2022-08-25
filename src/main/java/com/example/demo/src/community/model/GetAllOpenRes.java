package com.example.demo.src.community.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAllOpenRes {
    private int focusTagIdx;
    private List<CommunityTag> communityTags;
    private List<Posting> postingList;
}
