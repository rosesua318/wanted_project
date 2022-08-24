package com.example.demo.src.application;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.application.model.GetApplyRes;
import com.example.demo.src.application.model.GetWritingRes;
import com.example.demo.src.application.model.PostSearchApplyReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private final ApplicationProvider applicationProvider;

    @Autowired
    private final JwtService jwtService;


    public ApplicationController(ApplicationProvider applicationProvider, JwtService jwtService) {
        this.applicationProvider = applicationProvider;
        this.jwtService = jwtService;
    }

    /**
     * 지원 현황 조회(지원한 포지션) API
     * [GET] /applications/:userIdx
     * @return BaseResponse<GetApplyRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetApplyRes> getApplies(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetApplyRes getApplyRes = applicationProvider.getApplies(userIdx);
            return new BaseResponse<>(getApplyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 지원 현황 조회 (작성 중) API
     * [GET] /applications/writings/:userIdx
     * @return BaseResponse<GetWritingRes>
     */
    @ResponseBody
    @GetMapping("/writings/{userIdx}")
    public BaseResponse<GetWritingRes> getWritings(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetWritingRes getWritingRes = applicationProvider.getWritings(userIdx);
            return new BaseResponse<>(getWritingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 지원 현황 검색(지원한 포지션) API
     * [POST] /applications/:userIdx
     * @return BaseResponse<GetApplyRes>
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<GetApplyRes> searchApplies(@PathVariable("userIdx") int userIdx, @RequestBody PostSearchApplyReq postSearchApplyReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetApplyRes getApplyRes = applicationProvider.searchApplies(userIdx, postSearchApplyReq);
            return new BaseResponse<>(getApplyRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 지원 현황 검색 (작성 중) API
     * [POST] /applications/writings/:userIdx
     * @return BaseResponse<GetWritingRes>
     */
    @ResponseBody
    @PostMapping("/writings/{userIdx}")
    public BaseResponse<GetWritingRes> searchWritings(@PathVariable("userIdx") int userIdx, @RequestBody PostSearchApplyReq postSearchApplyReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetWritingRes getWritingRes = applicationProvider.searchWritings(userIdx, postSearchApplyReq);
            return new BaseResponse<>(getWritingRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
