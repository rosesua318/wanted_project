package com.example.demo.src.wanted;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.wanted.model.GetInterestTagRes;
import com.example.demo.src.wanted.model.GetWantedRes;
import com.example.demo.src.wanted.model.PatchInterestTagReq;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.config.BaseResponseStatus.PATCH_INTEREST_TAG_NO_DATA;

@RestController
@RequestMapping("/wanted")
public class WantedController {

    @Autowired
    private final WantedProvider wantedProvider;

    @Autowired
    private final WantedService wantedService;

    @Autowired
    private final JwtService jwtService;

    public WantedController(WantedProvider wantedProvider, WantedService wantedService, JwtService jwtService) {
        this.wantedProvider = wantedProvider;
        this.wantedService = wantedService;
        this.jwtService = jwtService;
    }

    /**
     * My 원티드 조회 API
     * [GET] /wanted/:userIdx
     * @return BaseResponse<GetWantedRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetWantedRes> getWanted(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetWantedRes getWantedRes = wantedProvider.getWanted(userIdx);
            return new BaseResponse<>(getWantedRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 관심 태그 설정 조회 API
     * [GET] /wanted/tags/:userIdx
     * @return BaseResponse<GetInterestTagRes>
     */
    @ResponseBody
    @GetMapping("/tags/{userIdx}")
    public BaseResponse<GetInterestTagRes> getInterestTag(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetInterestTagRes getInterestTagRes = wantedProvider.getInterestTag(userIdx);
            return new BaseResponse<>(getInterestTagRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 관심 태그 설정 API
     * [Patch] /wanted/tags/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/tags/{userIdx}")
    public BaseResponse<String> setInterestTag(@PathVariable("userIdx") int userIdx, @RequestBody PatchInterestTagReq patchInterestTagReq) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(patchInterestTagReq.getTags().size() == 0) {
                return new BaseResponse<>(PATCH_INTEREST_TAG_NO_DATA);
            }


            wantedService.setInterestTag(userIdx, patchInterestTagReq);
            return new BaseResponse<>("설정 변경 되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
