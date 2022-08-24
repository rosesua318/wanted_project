package com.example.demo.src.point;

import com.example.demo.config.BaseException;
import com.example.demo.src.point.model.GetPointRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class PointProvider {

    private final PointDao pointDao;

    public PointProvider(PointDao pointDao) {
        this.pointDao = pointDao;
    }

    public GetPointRes getPoints(int userIdx) throws BaseException {
        try {
            return pointDao.getPoints(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
