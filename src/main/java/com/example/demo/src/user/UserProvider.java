package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Transactional(readOnly = true)
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }



    public User.GetRes getUser(int userIdx) throws BaseException {
        try {
            User.GetRes getRes = userDao.getUser(userIdx);
            return getRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            int result = userDao.checkEmail(email);
            return result;
        } catch (Exception exception){
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public User.LoginRes logIn(User.LoginReq loginReq) throws BaseException{


        User.Info info = userDao.getPwd(loginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(loginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(info.getPassword().equals(encryptPwd)){
            int userIdx = info.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new User.LoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    // 전문분야 조회

    public GetSpecialtyRes getSpecialty(int userIdx) throws BaseException{

        try{
            GetSpecialtyRes getSpecialtyRes = userDao.getSpecialty(userIdx);
            return getSpecialtyRes;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
