package com.example.demo.src.resume;

import com.example.demo.config.BaseException;
import com.example.demo.src.resume.model.PatchResumeUpdateReq;
import com.example.demo.src.resume.model.PostResumeRes;
import com.example.demo.src.resume.model.ResumeTable;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ResumeService {


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResumeDao resumeDao;

    private final ResumeProvider resumeProvider;

    private final JwtService jwtService;

    @Autowired
    public ResumeService(ResumeDao resumeDao, ResumeProvider resumeProvider, JwtService jwtService) {
        this.resumeDao = resumeDao;
        this.resumeProvider = resumeProvider;
        this.jwtService = jwtService;
    }

    public PostResumeRes createResume(int userIdx) throws BaseException {

        try {
            int resumeIdx = resumeDao.createResume(userIdx);

            return new PostResumeRes(resumeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 이력서 각 종 Table(요소들) 생성하기

    public int createResumeTable(ResumeTable tableName, int Idx) throws BaseException {
        try {

            int createdIdx = resumeDao.createResumeTable(tableName, Idx);
            return createdIdx;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 이력서 각 종 Table(요소들) 삭제하기

    public void deleteResumeTable(ResumeTable tableName, int Idx) throws BaseException {

        try {
            int result = resumeDao.deleteResumeTable(tableName, Idx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_RESUMETABLE_STATUS);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 이력서 수정/작성(상세정보 등록)
    public boolean updateResume(PatchResumeUpdateReq patchResumeUpdateReq) throws BaseException{

        int resumeIdx = patchResumeUpdateReq.getResumeIntro().getResumeIdx();

        try{
            // userIdx 검증
             if(patchResumeUpdateReq.getUserIdx() != resumeDao.checkUser(resumeIdx)){
                throw new BaseException(INVALID_USER_JWT);
             }

             // 1. ResumeIntro
            if((patchResumeUpdateReq.getResumeIntro()!=null)){

             resumeDao.updateResumeInfo(patchResumeUpdateReq.getResumeIntro(), resumeIdx);
             }



            //2. Career
             if((patchResumeUpdateReq.getCareerList()!=null)){
                resumeDao.updateCareer(patchResumeUpdateReq.getCareerList());
             }

             // 3. CareerResult
            if((patchResumeUpdateReq.getCareerResultList()!=null)){
                resumeDao.updateCareerResult(patchResumeUpdateReq.getCareerResultList());
             }

             // 4. Education
            if((patchResumeUpdateReq.getEducationList()!=null)){
                resumeDao.updateEducation(patchResumeUpdateReq.getEducationList());

            }

            // 5. Award
            if((patchResumeUpdateReq.getAwardList()!=null)){
                resumeDao.updateAward(patchResumeUpdateReq.getAwardList());
            }

            // 6. ForeignLanguage
            if((patchResumeUpdateReq.getForeignLanguageList()!=null)){
                resumeDao.updateLanguage(patchResumeUpdateReq.getForeignLanguageList());
             }


            // 7. LanguageTest
            if((patchResumeUpdateReq.getLanguageTestList()!=null)){
                resumeDao.updateLanguageTest(patchResumeUpdateReq.getLanguageTestList());
            }

            // 8. 링크
            if((patchResumeUpdateReq.getResumeLinkList()!=null)){
                 resumeDao.updateLink(patchResumeUpdateReq.getResumeLinkList());
             }

            return true;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 이력서 삭제

    public void deleteResume(int resumeIdx) throws BaseException {

        try {
            int result = resumeDao.deleteResume(resumeIdx);
            if(result == 0){
                throw new BaseException(DELETE_FAIL_RESUME);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
