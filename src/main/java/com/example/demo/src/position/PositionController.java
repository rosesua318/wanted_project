package com.example.demo.src.position;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.position.model.GetOpenPositionRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private final PositionProvider positionProvider;

    private final JwtService jwtService;

    public PositionController(PositionProvider positionProvider, JwtService jwtService) {
        this.positionProvider = positionProvider;
        this.jwtService = jwtService;
    }

    /**
     * 채용 중인 포지션 조회 API (비회원용)
     * [GET] /positions
     *@returnBaseResponse<GetPositionRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetOpenPositionRes> getOpenPosition(@RequestParam(name = "jobIdx", defaultValue = "1") int jobIdx,
                                                 @RequestParam(name = "dutyIdx", defaultValue = "23") int dutyIdx) {
        try {
            if(jobIdx == 2 && dutyIdx == 23) {
                dutyIdx = 8;
            }
            GetOpenPositionRes getPositionRes = positionProvider.getPositionOpen(jobIdx, dutyIdx);
            return new BaseResponse<>(getPositionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 채용 중인 포지션 조회 API (회원용)
     * [GET] /positions/:userIdx
     *@returnBaseResponse<GetPositionRes>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetOpenPositionRes> getPosition(@PathVariable("userIdx") int userIdx,
                                                            @RequestParam(name = "jobIdx", defaultValue = "1") int jobIdx,
                                                    @RequestParam(name = "dutyIdx", defaultValue = "23") int dutyIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if(jobIdx == 2 && dutyIdx == 23) {
                dutyIdx = 8;
            }
            GetOpenPositionRes getPositionRes = positionProvider.getPosition(userIdx, jobIdx, dutyIdx);
            return new BaseResponse<>(getPositionRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
