package com.example.demo.src.resume;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.resume.model.GetResumeDetailRes;
import com.example.demo.src.resume.model.GetResumeListRes;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ResumeProvider {

    private final ResumeDao resumeDao;

    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ResumeProvider(ResumeDao resumeDao, JwtService jwtService) {
        this.resumeDao = resumeDao;
        this.jwtService = jwtService;
    }

    /**
     * 이력서 리스트 조회
     */

    public List<GetResumeListRes> getResumeList(int userIdx) throws BaseException {
        try {
            List<GetResumeListRes> getResumeListResList = resumeDao.getResumeList(userIdx);
            return getResumeListResList;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    /**
     * 이력서 상세 조회
     */

    public GetResumeDetailRes getResumeDetail(int userIdx, int resumeIdx) throws BaseException {
        try {
            GetResumeDetailRes getResumeDetailRes = resumeDao.getResumeDetail(userIdx, resumeIdx);
            return getResumeDetailRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 생성된 이력서가 있는 지 체크합니다.
//    public int checkResume(int resumeIdx) throws BaseException {
//
//        try {
//            return resumeDao.checkResume(resumeIdx);
//
//        } catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
}
