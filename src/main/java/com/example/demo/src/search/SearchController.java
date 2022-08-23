package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.search.model.GetCompanyTagHomeRes;
import com.example.demo.src.search.model.PostSearchTagReq;
import com.example.demo.src.search.model.PostSearchTagRes;
import com.example.demo.src.search.model.SearchTag;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/searches")
public class SearchController {

    @Autowired
    private final SearchProvider searchProvider;
    @Autowired
    private final SearchService searchService;

    @Autowired
    private final JwtService jwtService;

    public SearchController(SearchProvider searchProvider, SearchService searchService, JwtService jwtService) {
        this.searchProvider = searchProvider;
        this.searchService = searchService;
        this.jwtService = jwtService;
    }

    /**
     *회사 태그 검색(비회원용) API
     * [POST] /searches/tag
     *@returnBaseResponse<PostSearchTagRes>
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
     *회사 태그 검색(회원용) API
     * [POST] /searches/tag/:userIdx
     *@returnBaseResponse<PostSearchTagRes>
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

    /**
     *추천 태그 조회(비회원용) API
     * [GET] /searches
     *@returnBaseResponse<List<SearchTag>>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<List<SearchTag>> getRecommTag() throws BaseException {
        try {
            List<SearchTag> recommTags = searchProvider.getRecommTag();
            return new BaseResponse<>(recommTags);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *추천 태그 클릭 검색(비회원용) API
     * [GET] /tags/:tagIdx
     *@returnBaseResponse<PostSearchTagRes>
     */
    @ResponseBody
    @GetMapping("/tags/{tagIdx}")
    public BaseResponse<PostSearchTagRes> searchClickOpen(@PathVariable("tagIdx") int tagIdx) throws BaseException {
        try {
            PostSearchTagRes searchClickOpen = searchProvider.searchClickOpen(tagIdx);
            return new BaseResponse<>(searchClickOpen);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *추천 태그 클릭 검색(회원용) API
     * [GET] /tags/:tagIdx/:userIdx
     *@returnBaseResponse<PostSearchTagRes>
     */
    @ResponseBody
    @GetMapping("/tags/{tagIdx}/{userIdx}")
    public BaseResponse<PostSearchTagRes> searchClick(@PathVariable("tagIdx") int tagIdx, @PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PostSearchTagRes postSearchTags = searchProvider.searchClick(tagIdx, userIdx);
            return new BaseResponse<>(postSearchTags);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 기업 태그 홈 조회 (비회원용) API
     * [GET] /tags/home
     *@returnBaseResponse<GetCompanyTagHomeRes>
     */
    @ResponseBody
    @GetMapping("/tags/home")
    public BaseResponse<GetCompanyTagHomeRes> getCompanyTagHomeOpen() throws BaseException {
        try {
            GetCompanyTagHomeRes getCompanyTagHomeRes = searchProvider.getCompanyTagHomeOpen();
            return new BaseResponse<>(getCompanyTagHomeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 기업 태그 홈 조회 (회원용) API
     * [GET] /tags/home/:userIdx
     *@returnBaseResponse<GetCompanyTagHomeRes>
     */
    @ResponseBody
    @GetMapping("/tags/home/{userIdx}")
    public BaseResponse<GetCompanyTagHomeRes> getCompanyTagHome(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetCompanyTagHomeRes getCompanyTagHomeRes = searchProvider.getCompanyTagHome(userIdx);
            return new BaseResponse<>(getCompanyTagHomeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
