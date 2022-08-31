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

    public int checkPosting(int userIdx, int postingIdx) throws BaseException {
        try{
            return communityDao.checkPosting(userIdx, postingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkComment(int userIdx, int postingIdx, int commentIdx) throws BaseException {
        try{
            return communityDao.checkComment(userIdx, postingIdx, commentIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPostingActive(int userIdx, int postingIdx) throws BaseException {
        try{
            return communityDao.checkPostingActive(userIdx, postingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPostingForComment(int postingIdx) throws BaseException {
        try{
            return communityDao.checkPostingForComment(postingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkLikes(int userIdx, int postingIdx) throws BaseException{
        try{
            return communityDao.checkLikes(userIdx, postingIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
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

    public GetProfileRes getProfile(int userIdx) throws BaseException {
        try {
            return communityDao.getProfile(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMyPostRes getMyPost(int userIdx) throws BaseException {
        try {
            return communityDao.getMyPost(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMyCommentRes getMyComment(int userIdx) throws BaseException {
        try {
            return communityDao.getMyComment(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMyLikeRes getMyLike(int userIdx) throws BaseException {
        try {
            return communityDao.getMyLike(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetTagRes getTag(int postingIdx, int userIdx) throws BaseException {
        try {
            return communityDao.getTag(postingIdx, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
