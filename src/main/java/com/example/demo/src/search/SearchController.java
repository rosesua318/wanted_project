package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.position.model.GetOpenPositionRes;
import com.example.demo.src.search.model.PostSearchTagReq;
import com.example.demo.src.search.model.PostSearchTagRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/searches")
public class SearchController {

    @Autowired
    private final SearchService searchService;

    @Autowired
    private final JwtService jwtService;

    public SearchController(SearchService searchService, JwtService jwtService) {
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     * 회사 태그 검색 (비회원용) API
     * [POST] /searches/tag
     * @return BaseResponse<PostSearchTagRes>
     */
    @ResponseBody
    @PostMapping("/tags")
    public BaseResponse<PostSearchTagRes> searchTagOpen(@RequestBody PostSearchTagReq postSearchTagReq) throws BaseException {
        try {
            PostSearchTagRes postSearchTags = searchService.searchTagOpen(postSearchTagReq);
            return new BaseResponse<>(postSearchTags);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회사 태그 검색 (회원용) API
     * [POST] /searches/tag/:userIdx
     * @return BaseResponse<PostSearchTagRes>
     */
    @ResponseBody
    @PostMapping("/tags/{userIdx}")
    public BaseResponse<PostSearchTagRes> searchTag(@PathVariable("userIdx") int userIdx, @RequestBody PostSearchTagReq postSearchTagReq) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostSearchTagRes postSearchTags = searchService.searchTag(userIdx, postSearchTagReq);
            return new BaseResponse<>(postSearchTags);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
