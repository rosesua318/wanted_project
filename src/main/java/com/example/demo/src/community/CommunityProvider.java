package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.src.community.model.GetOtherOpenRes;
import com.example.demo.src.community.model.GetOtherRes;
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
}
