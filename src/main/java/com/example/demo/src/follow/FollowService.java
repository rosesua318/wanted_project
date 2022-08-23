package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.follow.model.PostFollowRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowService {


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;

    private final JwtService jwtService;

    @Autowired
    public FollowService(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }


    // 팔로우 등록하기

    public PostFollowRes createFollow(int userIdx, int companyIdx) throws BaseException {

        try{
            int followIdx =  followDao.createFollow(userIdx,companyIdx);
            return new PostFollowRes(followIdx);

        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 팔로우 삭제하기

    public void deleteFollow(int userIdx, int companyIdx) throws BaseException{
        try{
            followDao.deleteFollow(userIdx,companyIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
