package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.src.community.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class CommunityProvider {

    private final CommunityDao communityDao;

    public CommunityProvider(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }

    public GetOtherOpenRes getOtherTabOpen(int ctIdx) throws BaseException {
        try {
            return communityDao.getOtherTabOpen(ctIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOtherRes getOtherTab(int userIdx, int ctIdx) throws BaseException {
        try {
            return communityDao.getOtherTab(userIdx, ctIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAllOpenRes getAllTabOpen() throws BaseException {
        try {
            return communityDao.getAllTabOpen();
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAllRes getAllTab(int userIdx) throws BaseException {
        try {
            return communityDao.getAllTab(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetRecommOpenRes getRecommTabOpen() throws BaseException {
        try {
            return communityDao.getRecommTabOpen();
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetRecommRes getRecommTab(int userIdx) throws BaseException {
        try {
            return communityDao.getRecommTab(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPostingOpenRes getPostingOpen(int postingIdx) throws BaseException {
        try {
            return communityDao.getPostingOpen(postingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPostingRes getPosting(int postingIdx, int userIdx) throws BaseException {
        try {
            return communityDao.getPosting(postingIdx, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
