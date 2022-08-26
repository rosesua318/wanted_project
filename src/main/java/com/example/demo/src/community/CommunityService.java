package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.src.community.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class CommunityService {
    private final CommunityDao communityDao;

    public CommunityService(CommunityDao communityDao) {
        this.communityDao = communityDao;
    }

    public void setProfile(int userIdx, PatchProfileReq patchProfileReq) throws BaseException {
        try {
            communityDao.setProfile(userIdx, patchProfileReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPostingRes createPosting(int userIdx, PostPostingReq postPostingReq) throws BaseException {
        try {
            return communityDao.createPosting(userIdx, postPostingReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCommentRes createComment(int postingIdx, int userIdx, PostCommentReq postCommentReq) throws BaseException {
        try {
            return communityDao.createComment(postingIdx, userIdx, postCommentReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PutPostingRes updatePosting(int userIdx, PutPostingReq putPostingReq) throws BaseException {
        try {
            return communityDao.updatePosting(userIdx, putPostingReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPostingRes createPostingWithImage(int userIdx, String imageUrl, PostPostingReq postPostingReq) throws BaseException {
        try {
            return communityDao.createPostingWithImage(userIdx, imageUrl, postPostingReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PutPostingRes updatePostingWithImage(int userIdx, String imageUrl, PutPostingReq putPostingReq) throws BaseException {
        try {
            return communityDao.updatePostingWithImage(userIdx, imageUrl, putPostingReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePosting(int userIdx, int postingIdx) throws BaseException {
        try {
            communityDao.deletePosting(userIdx, postingIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteComment(int userIdx, int postingIdx, int commentIdx) throws BaseException {
        try {
            communityDao.deleteComment(userIdx, postingIdx, commentIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
