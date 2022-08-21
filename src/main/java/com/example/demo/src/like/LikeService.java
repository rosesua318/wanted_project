package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.LikeDao;
import com.example.demo.src.like.LikeProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_LIKES;

@Service
@Transactional
public class LikeService {
    private final LikeDao likeDao;
    private final LikeProvider likeProvider;


    public LikeService(LikeDao likeDao, LikeProvider likeProvider) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
    }

    public int modifyLikes(int userIdx, int employmentIdx) throws BaseException {
        try {
            int likeIdx = likeDao.modifyLikes(userIdx, employmentIdx);
            return likeIdx;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int createLikes(int userIdx, int employmentIdx) throws BaseException {
        try {
            int likeIdx = likeDao.createLikes(userIdx, employmentIdx);
            return likeIdx;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteLikes(int userIdx, int employmentIdx) throws BaseException {
        try {
            likeDao.deleteLikes(userIdx, employmentIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
