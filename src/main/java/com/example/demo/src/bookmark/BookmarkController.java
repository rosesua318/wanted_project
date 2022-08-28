package com.example.demo.src.bookmark;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.bookmark.model.*;
import com.example.demo.src.employment.EmploymentProvider;
import com.example.demo.src.employment.model.GetEmploymentInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.POST_BOOKMARK_CREATE_FAIL;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BookmarkProvider bookmarkProvider;

    @Autowired
    private final BookmarkService bookmarkService;

    @Autowired
    private final BookmarkDao bookmarkDao;

    @Autowired
    private final EmploymentProvider employmentProvider;
    @Autowired
    private final JwtService jwtService;

    public BookmarkController(BookmarkProvider bookmarkProvider, BookmarkService bookmarkService, BookmarkDao bookmarkDao, EmploymentProvider employmentProvider, JwtService jwtService) {
        this.bookmarkProvider = bookmarkProvider;
        this.bookmarkService = bookmarkService;
        this.bookmarkDao = bookmarkDao;
        this.employmentProvider = employmentProvider;
        this.jwtService = jwtService;
    }


    /**
     * 북마크 등록 API
     */

    @ResponseBody
    @PostMapping("{userIdx}") // POST localhost:9000/bookmarks/:userIdx;
    public BaseResponse<PostBookmarkRes> createBookmark(@RequestBody PostBookMarkReq postBookMarkReq) {
        // 북마크 할 채용 공고를 입력하지 않은 경우.
        if (postBookMarkReq.getEmploymentIdx() == 0) {
            return new BaseResponse<>(POST_BOOKMARK_CREATE_FAIL);
        }
        try{
            PostBookmarkRes postBookmarkRes = bookmarkService.createBookmark(postBookMarkReq);
            return new BaseResponse<>(postBookmarkRes);
        } catch(BaseException exception){

            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 북마크 조회 API
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetEmploymentInfoRes>> getBookmarkList(@PathVariable("userIdx") int userIdx) throws BaseException {
        //  jwt에서 idx 추출.

        int userIdxByJwt = jwtService.getUserIdx();
        //userIdx와 접근한 유저가 같은지 확인
        if (userIdx != userIdxByJwt) {
            return new BaseResponse<>(INVALID_USER_JWT);
        }
        try {
            List<GetEmploymentInfoRes> employmentInfo = employmentProvider.getEmploymentInfoList(userIdx);
            return new BaseResponse<>(employmentInfo);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     *
    북마크 삭제 API
     */

    @ResponseBody
    @PatchMapping("/status/{userIdx}") //
    public BaseResponse<String> DeleteBookmark(@PathVariable("userIdx") int userIdx, @RequestBody BookmarkStatus bookmarkStatus) throws BaseException {

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            PatchBookmarkStatusReq patchBookmarkStatusReq = new PatchBookmarkStatusReq(bookmarkStatus.getBookmarkIdx());
            bookmarkService.DeleteBookmark(patchBookmarkStatusReq);

            String result = "북마크 삭제 완료";
            return new BaseResponse<>(result);

        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
