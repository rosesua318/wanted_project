package com.example.demo.src.wanted;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.wanted.model.GetWantedRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/wanted")
public class WantedController {

    @Autowired
    private final WantedProvider wantedProvider;

    @Autowired
    private final JwtService jwtService;

    public WantedController(WantedProvider wantedProvider, JwtService jwtService) {
        this.wantedProvider = wantedProvider;
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
}
