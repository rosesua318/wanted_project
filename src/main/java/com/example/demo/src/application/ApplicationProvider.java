package com.example.demo.src.application;

import com.example.demo.config.BaseException;
import com.example.demo.src.application.model.GetApplyRes;
import com.example.demo.src.application.model.GetWritingRes;
import com.example.demo.src.application.model.PostSearchApplyReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class ApplicationProvider {
    private final ApplicationDao applicationDao;

    public ApplicationProvider(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    public GetApplyRes getApplies(int userIdx) throws BaseException {
        try {
            return applicationDao.getApplies(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetWritingRes getWritings(int userIdx) throws BaseException {
        try {
            return applicationDao.getWritings(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetApplyRes searchApplies(int userIdx, PostSearchApplyReq postSearchApplyReq) throws BaseException {
        try {
            return applicationDao.searchApplies(userIdx, postSearchApplyReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetWritingRes searchWritings(int userIdx, PostSearchApplyReq postSearchApplyReq) throws BaseException {
        try {
            return applicationDao.searchWritings(userIdx, postSearchApplyReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
