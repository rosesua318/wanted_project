package com.example.demo.src.position;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.position.model.GetOpenPositionRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/positions")
public class PositionController {

    @Autowired
    private final PositionProvider positionProvider;

    public PositionController(PositionProvider positionProvider) {
        this.positionProvider = positionProvider;
    }

    /**
     * 채용 중인 포지션 조회 API (비회원용)
     * [GET] /positions
     *@returnBaseResponse<GetPositionRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetOpenPositionRes> getHome(@RequestParam(name = "jobIdx", defaultValue = "1") int jobIdx,
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
}
