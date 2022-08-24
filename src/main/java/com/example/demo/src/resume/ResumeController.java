package com.example.demo.src.resume;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.resume.model.GetResumeDetailRes;
import com.example.demo.src.resume.model.GetResumeListRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/resumes")
public class ResumeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ResumeProvider resumeProvider;
    @Autowired
    private final ResumeService resumeService;
    @Autowired
    private final JwtService jwtService;

    public ResumeController(ResumeProvider resumeProvider, ResumeService resumeService, JwtService jwtService) {
        this.resumeProvider = resumeProvider;
        this.resumeService = resumeService;
        this.jwtService = jwtService;
    }

    /**
     * 이력서 리스트 조회
     */

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetResumeListRes>> getResumeList(@PathVariable("userIdx") int userIdx){

        try{
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
         //   }
            List<GetResumeListRes> getResumeListRes = resumeProvider.getResumeList(userIdx);
            return new BaseResponse<>(getResumeListRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 이력서 상세 조회
     */

    @ResponseBody
    @GetMapping("/{userIdx}/{resumeIdx}")
    public BaseResponse<GetResumeDetailRes> getResumeDetail(@PathVariable("userIdx") int userIdx, @PathVariable("resumeIdx") int resumeIdx){

        try {
            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(INVALID_USER_JWT);
            //     }
            GetResumeDetailRes getResumeDetailRes = resumeProvider.getResumeDetail(userIdx, resumeIdx);
            return new BaseResponse<>(getResumeDetailRes);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

}
