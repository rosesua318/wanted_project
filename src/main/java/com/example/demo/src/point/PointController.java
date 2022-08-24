package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired
    private final PointProvider pointProvider;

    @Autowired
    private final JwtService jwtService;

    public PointController(PointProvider pointProvider, JwtService jwtService) {
        this.pointProvider = pointProvider;
        this.jwtService = jwtService;
    }

    /**
     * 포인트 조회 API
     * [GET] /points/:userIdx
     * @return BaseResponse<GetPointRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetPointRes> getLikes(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetPointRes getPointRes = pointProvider.getPoints(userIdx);
            return new BaseResponse<>(getPointRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}
