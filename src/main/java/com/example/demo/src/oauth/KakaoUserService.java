package com.example.demo.src.oauth;


import com.example.demo.config.BaseException;
import com.example.demo.src.oauth.model.KakaoUserLoginRes;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Transactional
@Service
public class KakaoUserService {


    private final KakaoUserDao kakaoUserDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public KakaoUserService(KakaoUserDao kakaoUserDao, JwtService jwtService){
        this.kakaoUserDao = kakaoUserDao;
        this.jwtService = jwtService;
    }



    public KakaoUserLoginRes createKakaoUser(String nickname, Object email) throws BaseException {

        try {
            int userIdx = kakaoUserDao.createKakaoUser(nickname, email);

            String jwt = jwtService.createJwt(userIdx);
            return new KakaoUserLoginRes(userIdx,jwt);

        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);

        }
    }
    public KakaoUserLoginRes logIn(String nickname, String email) throws BaseException {


        if (kakaoUserDao.checkEmail(email) == 0) {
            createKakaoUser(nickname, email);
        }
        int userIdx = kakaoUserDao.getEmail(email);
        String jwt = jwtService.createJwt(userIdx);
        return new KakaoUserLoginRes(userIdx, jwt);
    }
}
