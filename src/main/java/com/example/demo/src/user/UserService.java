package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    // 유저 로그인 - 이메일 체크

    public String checkEmail(String email) throws BaseException {
        String result;
        if(userProvider.checkEmail(email) == 0){
            result = "회원가입부터 진행해야 합니다.";
        }
        else
            result = "비밀번호 입력";
        return result;
    }

    //POST
    public User.PostResponse createUser(User.Request request) throws BaseException {
        //중복
        if(userProvider.checkEmail(request.getEmail()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(request.getPassword());
            request.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(request);
            //jwt 발급.
            String jwt = jwtService.createJwt(userIdx);
            return new User.PostResponse(jwt,userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserInfo(User.PatchReq patchReq) throws BaseException {
        try{
            int result = userDao.modifyUserInfo(patchReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저 상태 변경
    public void modifyUserStatus(User.StatusReq statusReq) throws BaseException {
        try {
            int result = userDao.modifyUserStatus(statusReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_STATUS);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPwd(User.PatchPwdReq patchPwdReq) throws BaseException{

        // 비밀번호 암호화
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(patchPwdReq.getPassword());
            patchPwdReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int result = userDao.modifyPwd(patchPwdReq);
            if(result ==0){
                throw new BaseException(MODIFY_FAIL_PWD);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 프로필 이미지 변경

    public void modifyUserImage(User.PatchImage patchImage) throws BaseException{

        int userIdx = patchImage.getUserIdx();
        try{
            // userIdx 검증 (탈퇴한 회원이거나 등)
            if(userIdx != userDao.checkUser(userIdx)){
                throw new BaseException(INVALID_USER_INACTIVE);
            }

            int result = userDao.modifyUserImage(patchImage);
            if(result ==0){
                throw new BaseException(MODIFY_FAIL_USER_IMAGE);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 계정 공개,비공개 설정

    public void modifyUserIsPrivate(User.PatchPrivate patchPrivate) throws BaseException{
        int userIdx = patchPrivate.getUserIdx();

        try{
            // userIdx 검증 (탈퇴한 회원이거나 등)
            if(userIdx != userDao.checkUser(userIdx)){
                throw new BaseException(INVALID_USER_INACTIVE);
            }
            int result = userDao.modifyUserIsPrivate(patchPrivate);
            if(result ==0){
                throw new BaseException(MODIFY_FAIL_USER_PRIVATE);
            }
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

  //   전문분야 생성
    public int createSpecialty(User.SpecialtyReq specialtyReq) throws BaseException{

        try{
            int specialtyIdx = userDao.createSpecialty(specialtyReq);

            return specialtyIdx;

        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 구직여부 설정
    public void modifyJobSearchStatus(PatchJobsStatusReq patchJobsStatusReq) throws BaseException{

        try {
            int result = userDao.modifyJobSearchStatus(patchJobsStatusReq);
            if(result ==0)
                throw new BaseException(MODIFY_FAIL_JOBSEARCH_STATUS);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
