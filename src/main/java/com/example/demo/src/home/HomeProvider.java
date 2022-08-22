package com.example.demo.src.home;

import com.example.demo.config.BaseException;
import com.example.demo.src.home.model.GetHomeRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class HomeProvider {
    private final HomeDao homeDao;

    public HomeProvider(HomeDao homeDao) {
        this.homeDao = homeDao;
    }

    public GetHomeRes getHome(int homecategoryIdx) throws BaseException {
        try {
            GetHomeRes getHomeRes = homeDao.getHome(homecategoryIdx);
            return getHomeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
