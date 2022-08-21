package com.example.demo.src.bookmark.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostBookMarkReq {

    private int employmentIdx; // 채용 공고 포지션 Idx
    private int userIdx;
}
