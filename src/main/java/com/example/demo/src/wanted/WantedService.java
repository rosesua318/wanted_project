package com.example.demo.src.wanted;

import com.example.demo.config.BaseException;
import com.example.demo.src.wanted.model.PatchInterestTagReq;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class WantedService {
    private final WantedDao wantedDao;

    public WantedService(WantedDao wantedDao) {
        this.wantedDao = wantedDao;
    }

    public void setInterestTag(int userIdx, PatchInterestTagReq patchInterestTagReq) throws BaseException {
        try {
            wantedDao.setInterestTag(userIdx, patchInterestTagReq.getTags());
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
