package com.example.demo.src.wanted;

import com.example.demo.config.BaseException;
import com.example.demo.src.wanted.model.GetWantedRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class WantedProvider {
    private final WantedDao wantedDao;

    public WantedProvider(WantedDao wantedDao) {
        this.wantedDao = wantedDao;
    }

    public GetWantedRes getWanted(int userIdx) throws BaseException {
        try {
            return wantedDao.getWanted(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
