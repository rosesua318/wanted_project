package com.example.demo.src.bookmark.model;

import lombok.*;

public class Bookmark {

    // 북마크 등록 요청 값
    @Getter
    @Setter
    public static class Request{ // 기존 PostBookMarkReq
        private int employmentIdx; // 채용 공고 포지션 Idx
        private int userIdx;
    }


   // 북마크 등록 반환값
    @Getter
    @AllArgsConstructor
    public static class Response{ // 기존 PostBookmarkRes
        private int bookmarkIdx;
    }

    // 북마크 상태변경
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookmarkStatus{
        private int employmentIdx;
        private String status;

        public BookmarkStatus(int employmentIdx) {
            this.employmentIdx = employmentIdx;
        }
    }


}
