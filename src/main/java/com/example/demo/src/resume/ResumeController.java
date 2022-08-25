package com.example.demo.src.resume;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.resume.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_IDX;
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
    public BaseResponse<List<GetResumeListRes>> getResumeList(@PathVariable("userIdx") int userIdx) {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
               }
            List<GetResumeListRes> getResumeListRes = resumeProvider.getResumeList(userIdx);
            return new BaseResponse<>(getResumeListRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 이력서 상세 조회
     */

    @ResponseBody
    @GetMapping("/{userIdx}/{resumeIdx}")
    public BaseResponse<GetResumeDetailRes> getResumeDetail(@PathVariable("userIdx") int userIdx, @PathVariable("resumeIdx") int resumeIdx) {

        try {
     //       jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
                 }
            GetResumeDetailRes getResumeDetailRes = resumeProvider.getResumeDetail(userIdx, resumeIdx);
            return new BaseResponse<>(getResumeDetailRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 이력서 생성
     */

    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostResumeRes> createResume(@PathVariable("userIdx") int userIdx, @RequestBody PostResumeReq postResumeReq) {

        try {
            //        jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            if (userIdx != postResumeReq.getUserIdx()) {
                return new BaseResponse<>(INVALID_USER_IDX);
            }

            PostResumeRes postResumeRes = resumeService.createResume(userIdx);
            return new BaseResponse<>(postResumeRes);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 경력(empty)생성

    @ResponseBody
    @PostMapping("/{resumeIdx}/careers")
    public BaseResponse<String> createCareer(@PathVariable("resumeIdx") int resumeIdx) {

        try {
            ResumeTable table = ResumeTable.Career;
            int careerIdx = resumeService.createResumeTable(table, resumeIdx);

            String result = (table.getTableName() + "Idx:" + careerIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 경력 성과 생성 (empty)
    @ResponseBody
    @PostMapping("/{resumeIdx}/careers/{careerIdx}/results")
    public BaseResponse<String> createCareerResult(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("careerIdx") int careerIdx) {

        try {
            ResumeTable table = ResumeTable.CareerResult;
            int careerResultIdx = resumeService.createResumeTable(table, careerIdx);

            String result = (table.getTableName() + "Idx:" + careerResultIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 학력 생성 (empty)
    @ResponseBody
    @PostMapping("/{resumeIdx}/educations")
    public BaseResponse<String> createEducation(@PathVariable("resumeIdx") int resumeIdx) {

        try {
            ResumeTable table = ResumeTable.Education;
            int educationIdx = resumeService.createResumeTable(table, resumeIdx);

            String result = (table.getTableName() + "Idx:" + educationIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 수상 및 기타 생성 (empty)

    @ResponseBody
    @PostMapping("/{resumeIdx}/awards")
    public BaseResponse<String> createAward(@PathVariable("resumeIdx") int resumeIdx) {

        try {
            ResumeTable table = ResumeTable.Award;
            int awardIdx = resumeService.createResumeTable(table, resumeIdx);

            String result = (table.getTableName() + "Idx:" + awardIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 외국어 생성 (empty)

    @ResponseBody
    @PostMapping("/{resumeIdx}/languages")
    public BaseResponse<String> createLanguage(@PathVariable("resumeIdx") int resumeIdx) {

        try {
            ResumeTable table = ResumeTable.Language;
            int languageIdx = resumeService.createResumeTable(table, resumeIdx);

            String result = (table.getTableName() + "Idx:" + languageIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }


    }

    // 이력서 어학시험 생성(empty)
    @ResponseBody
    @PostMapping("/{resumeIdx}/languages/{languageIdx}/tests")
    public BaseResponse<String> createLanguageTest(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("languageIdx") int languageIdx) {

        try {
            ResumeTable table = ResumeTable.LanguageTest;
            int languageTestIdx = resumeService.createResumeTable(table, languageIdx);

            String result = (table.getTableName() + "Idx:" + languageTestIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 링크 생성(empty)
    @ResponseBody
    @PostMapping("/{resumeIdx}/links")
    public BaseResponse<String> createLink(@PathVariable("resumeIdx") int resumeIdx) {

        try {
            ResumeTable table = ResumeTable.Link;
            int linkIdx = resumeService.createResumeTable(table, resumeIdx);

            String result = (table.getTableName() + "Idx:" + linkIdx);
            return new BaseResponse<>(result);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    // 이력서 경력 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/careers/{careerIdx}")
    public BaseResponse<String> deleteCareer(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("careerIdx") int Idx){
        try{
            ResumeTable table = ResumeTable.Career;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 경력 성과 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/careers/{careerIdx}/results/{resultIdx}")
    public BaseResponse<String> deleteCareerResult(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("careerIdx") int careerIdx, @PathVariable("resultIdx") int Idx){
        try{
            ResumeTable table = ResumeTable.CareerResult;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 학력 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/educations/{educationIdx}")
    public BaseResponse<String> deleteEducation(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("educationIdx") int Idx){
        try{
            ResumeTable table = ResumeTable.Education;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 수상 및 기타 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/awards/{awardIdx}")
    public BaseResponse<String> deleteAward(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("awardIdx") int Idx){
        try{
            ResumeTable table = ResumeTable.Award;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 외국어 삭제

    @ResponseBody
    @PatchMapping ("/{resumeIdx}/languages/{languageIdx}")
    public BaseResponse<String> deleteLanguage(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("languageIdx") int Idx){
        try{

            ResumeTable table = ResumeTable.Language;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 어학시험 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/languages/{languageIdx}/tests/{testIdx}")
    public BaseResponse<String> deleteLanguage(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("languageIdx") int languageIdx, @PathVariable("testIdx") int Idx){
        try{

            ResumeTable table = ResumeTable.LanguageTest;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    // 이력서 링크 삭제
    @ResponseBody
    @PatchMapping ("/{resumeIdx}/links/{linkIdx}")
    public BaseResponse<String> deleteLink(@PathVariable("resumeIdx") int resumeIdx, @PathVariable("linkIdx") int Idx){
        try{

            ResumeTable table = ResumeTable.Link;
            resumeService.deleteResumeTable(table,Idx);
            String result = (table.getTableName() + " 삭제되었습니다.");
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
