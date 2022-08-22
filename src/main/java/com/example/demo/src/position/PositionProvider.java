package com.example.demo.src.position;


import com.example.demo.config.BaseException;
import com.example.demo.src.position.model.GetOpenPositionRes;
import com.example.demo.utils.JwtService;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PositionProvider {
    private final PositionDao positionDao;

    private final JwtService jwtService;

    public PositionProvider(PositionDao positionDao, JwtService jwtService) {
        this.positionDao = positionDao;
        this.jwtService = jwtService;
    }

    public GetOpenPositionRes getPositionOpen(int jobIdx, int dutyIdx) throws BaseException {
        try {
            GetOpenPositionRes getOpenPositionRes = positionDao.getPositionOpen(jobIdx, dutyIdx);
            return getOpenPositionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOpenPositionRes getPosition(int userIdx, int jobIdx, int dutyIdx) throws BaseException {
        try {
            GetOpenPositionRes getOpenPositionRes = positionDao.getPosition(userIdx, jobIdx, dutyIdx);
            return getOpenPositionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
