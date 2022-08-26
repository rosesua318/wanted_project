package com.example.demo.src.employment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.employment.model.GetEmpDetailRes;
import com.example.demo.src.employment.model.GetEmpHomeRes;
import com.example.demo.src.employment.model.PostApplicationReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/employments")
public class EmploymentController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final EmploymentProvider employmentProvider;

    @Autowired
    private final EmploymentDao employmentDao;

    @Autowired
    private final EmploymentService employmentService;

    @Autowired
    private final JwtService jwtService;

    public EmploymentController(EmploymentProvider employmentProvider, EmploymentDao employmentDao, EmploymentService employmentService, JwtService jwtService) {
        this.employmentProvider = employmentProvider;
        this.employmentDao = employmentDao;
        this.employmentService = employmentService;
        this.jwtService = jwtService;
    }

    /**
     * 비회원일 시에 채용 페이지 화면 조회
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetEmpHomeRes> getEmpHome() {

        try{
            GetEmpHomeRes getEmpHomeRes = employmentProvider.getEmpHome();
            return new BaseResponse<>(getEmpHomeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원 일 시 채용 화면 조회
     * @return
     */

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetEmpHomeRes> getEmpHome(@PathVariable("userIdx") int userIdx) {

        try{
            GetEmpHomeRes getEmpHomeRes = employmentProvider.getEmpHome(userIdx);
            return new BaseResponse<>(getEmpHomeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 채용 정보 조회(포지션)
     */

    @ResponseBody
    @GetMapping("/{userIdx}/{employmentIdx}")
    public BaseResponse<GetEmpDetailRes> getEmpDetail(@PathVariable("userIdx") int userIdx, @PathVariable("employmentIdx") int employmentIdx) {

        try{
            GetEmpDetailRes getEmpDetailRes = employmentProvider.getEmpDetail(userIdx,employmentIdx);
            return new BaseResponse<>(getEmpDetailRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 지원하기
     */

    @ResponseBody
    @PostMapping("/{userIdx}/{employmentIdx}")
    public BaseResponse<String> createApplicant(@PathVariable("userIdx") int userIdx, @PathVariable("employmentIdx") int employmentIdx, @RequestBody PostApplicationReq postApplicationReq){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int applicantIdx = employmentService.createApplicant(postApplicationReq);
            String result = "지원 완료 ApplicantIdx : " + applicantIdx;

            return new BaseResponse<>(result);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
