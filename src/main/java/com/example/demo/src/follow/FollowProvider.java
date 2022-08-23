package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(readOnly = true)
public class FollowProvider {

    private final FollowDao followDao;

    public FollowProvider(FollowDao followDao) {
        this.followDao = followDao;
    }

    // 이미 등록된 팔로우인지 check

    public int checkFollows(int userIdx, int companyIdx) throws BaseException {
        try{
            return followDao.checkFollows(userIdx,companyIdx);
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
