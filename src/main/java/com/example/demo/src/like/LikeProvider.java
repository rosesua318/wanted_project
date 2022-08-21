package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetLikeRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class LikeProvider {
    private final LikeDao likeDao;

    public LikeProvider(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public int checkLikes(int userIdx, int employmentIdx) throws BaseException{
        try{
            return likeDao.checkLikes(userIdx, employmentIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetLikeRes> getLikes(int userIdx) throws BaseException {
        try {
            List<GetLikeRes> getLikeRes = likeDao.getLikes(userIdx);
            return getLikeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
