package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;
import static com.example.demo.utils.ValidationRegex.isRegexPassword;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원 프로필 조회
     *
     * @param userIdx
     * @return
     **/
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        // 일단은 가장 기본적인 이름,이메일,전화번호만 조회하도록 함. 이후 이력서 등등.. 구현 예정
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {


        // 1. 이메일 주소
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // 2. 아이디(로그인 아이디)
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_ID);
        }

        // 3. 비밀번호
        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        // 4. 전화번호
        if(postUserReq.getPhone() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }
    //    (테스트시의 불편함으로 잠깐 해제)
        //이메일 정규표현
//        if(isRegexEmail(postUserReq.getEmail())){  // 정규표현식과 다른 형식으로 받으면 invalid (이메일 주소 형식)
//            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
//        }
        //비밀번호 정규표현
//        if (isRegexPassword(postUserReq.getPassword())){  // 특수문자 / 문자 / 숫자 포함 형태의 8~20자리 이내의 암호 정규식
//            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
//        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{


            // 기본 validation. 아이디(이메일) , 비밀번호를 입력하지 않은 경우
            if(postLoginReq.getEmail() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }


            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
           // 변경 사항 : 이름, 이메일, 전화번호(현재 전화번호 인증하기 기능 제외)
            PatchUserReq patchUserReq = new PatchUserReq(user.getUserIdx(),user.getName(),user.getEmail(),user.getPhone());
            userService.modifyUserInfo(patchUserReq);

            String result = "회원 정보 수정 완료되었습니다.";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 비밀번호 변경
     */
    @ResponseBody
    @PatchMapping("/pwd/{userIdx}") //
    public BaseResponse<String> modifyUserStatus(@PathVariable("userIdx") int userIdx, @RequestBody PwdModify pwdModify){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }  // 이 부분까지는 유저가 사용하는 기능 중 유저에 대한 보안이 철저히 필요한 api 에서 사용

            PatchPwdReq patchPwdReq = new PatchPwdReq(userIdx, pwdModify.getPassword());
            userService.modifyPwd(patchPwdReq);

            String result = "비밀번호 변경이 완료되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 회원 탈퇴 API
     */

    @ResponseBody
    @PatchMapping("/status/{userIdx}") // PATCH /users/status/:userIdx
    public BaseResponse<String> modifyUserStatus(@PathVariable("userIdx") int userIdx, @RequestBody UserStatus userStatus){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }  // 이 부분까지는 유저가 사용하는 기능 중 유저에 대한 보안이 철저히 필요한 api 에서 사용

            PatchUserStatusReq patchUserStatusReq = new PatchUserStatusReq(userIdx, userStatus.getStatus());
            userService.modifyUserStatus(patchUserStatusReq);

            String result = "탈퇴가 완료되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
