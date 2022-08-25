package com.example.demo.src.community;

import com.example.demo.config.BaseException;
import com.example.demo.src.community.model.PatchProfileReq;
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
}
