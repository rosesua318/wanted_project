package com.example.demo.src.position;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;


import com.example.demo.src.position.model.GetOpenPositionRes;

import com.example.demo.src.position.model.*;

import com.example.demo.src.position.model.*;

import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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

    /**
     * 직군 조회 API
     * [GET] /positions/job-group
     *@returnBaseResponse<List<JobCategory>>
     */
    @ResponseBody
    @GetMapping("/job-group")
    public BaseResponse<List<JobCategory>> getJobCategory() {
        try {
            List<JobCategory> jobCategories = positionProvider.getJobCategory();
            return new BaseResponse<>(jobCategories);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 직무 조회 API
     * [GET] /positions/duties/:jobIdx
     *@returnBaseResponse<List<DutyCategory>>
     */
    @ResponseBody
    @GetMapping("/duties/{jobIdx}")
    public BaseResponse<List<DutyCategory>> getDutyCategory(@PathVariable("jobIdx") int jobIdx) {
        try {
            List<DutyCategory> dutyCategories = positionProvider.getDutyCategory(jobIdx);
            return new BaseResponse<>(dutyCategories);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 국가 조회 API
     * [GET] /positions/nations
     *@returnBaseResponse<List<Nation>>
     */
    @ResponseBody
    @GetMapping("/nations")
    public BaseResponse<List<Nation>> getNation() {
        try {
            List<Nation> nations = positionProvider.getNations();
            return new BaseResponse<>(nations);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 지역 조회 API
     * [GET] /positions/regions/:nationIdx
     *@returnBaseResponse<List<Region>>
     */
    @ResponseBody
    @GetMapping("/regions/{nationIdx}")
    public BaseResponse<List<Region>> getRegion(@PathVariable("nationIdx") int nationIdx) {
        try {
            List<Region> regions = positionProvider.getRegions(nationIdx);
            return new BaseResponse<>(regions);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 상세지역 조회 API
     * [GET] /positions/detail-regions/:regionIdx
     *@returnBaseResponse<List<DetailRegion>>
     */
    @ResponseBody
    @GetMapping("/detail-regions/{regionIdx}")
    public BaseResponse<List<DetailRegion>> getDetailRegion(@PathVariable("regionIdx") int regionIdx) {
        try {
            List<DetailRegion> detailRegions = positionProvider.getDetailRegions(regionIdx);
            return new BaseResponse<>(detailRegions);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 기술스택 조회 API
     * [GET] /positions/stacks
     *@returnBaseResponse<List<EmpStack>>
     */
    @ResponseBody
    @GetMapping("/stacks")
    public BaseResponse<List<EmpStack>> getStack() {
        try {
            List<EmpStack> stacks = positionProvider.getStacks();
            return new BaseResponse<>(stacks);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
