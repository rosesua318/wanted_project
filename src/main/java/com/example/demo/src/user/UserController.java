package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;




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
     *
     *
     **/
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/users/:userIdx
    public BaseResponse<User.GetRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        // 일단은 가장 기본적인 이름,이메일,전화번호만 조회하도록 함. 이후 이력서 등등.. 구현 예정
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            User.GetRes getRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */

    @ResponseBody
    @PostMapping("")
    public BaseResponse<User.PostResponse> createUser(@RequestBody User.Request request) {


        try {
            // @Valid 를 이용하고 싶었지만, 템플릿 구조 상 ControllerAdvice를 사용하긴 애매해서 if문으로 처리.
        // 1. 이메일 주소
        if (request.getEmail() == null || request.getEmail() =="") {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }

        // 2. 비밀번호
        if (request.getPassword() == null || request.getPassword() == "") {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        // 3. 이름
        if(request.getName() == null || request.getName() == ""){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }

        // 4. 전화번호
        if (request.getPhone() == null || request.getPhone() == "") {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }

        // 이메일 정규표현
        if (!isRegexEmail(request.getEmail())) {  // 정규표현식과 다른 형식으로 받으면 invalid (이메일 주소 형식)
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        //  비밀번호 정규표현
        if (!isRegexPassword(request.getPassword())) {  // 특수문자 / 문자 / 숫자 포함 형태의 8~20자리 이내의 암호 정규식
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }
            User.PostResponse response = userService.createUser(request);
            return new BaseResponse<>(response);
        } catch (BaseException exception) {
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
    public BaseResponse<User.LoginRes> logIn(@RequestBody User.LoginReq loginReq){
        try{

            // 기본 validation. 아이디(이메일) , 비밀번호를 입력하지 않은 경우
            if(loginReq.getEmail() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            if(loginReq.getPassword() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            if(userProvider.checkEmail(loginReq.getEmail())==0){
                return new BaseResponse<>(FAILED_TO_LOGIN);
            }
            User.LoginRes loginRes = userProvider.logIn(loginReq);
            return new BaseResponse<>(loginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 로그인 API - 이메일 확인
     */

    @ResponseBody
    @PostMapping("/login/email")
    public BaseResponse<String> logInEmail(@RequestBody User.LoginEmailReq loginEmailReq){
        String email = loginEmailReq.getEmail();
        try{
            if(email == null){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            String result = userService.checkEmail(email);
            return new BaseResponse<>(result);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User.PatchReq patchReq){
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // body,파라미터 유저 인덱스 검증
            if (userIdx != patchReq.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_IDX);
            }

           // 변경 사항 : 이름, 이메일, 전화번호(현재 전화번호 인증하기 기능 제외)
            userService.modifyUserInfo(patchReq);
            String result = "회원 정보 수정 완료되었습니다.";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 비밀번호 변경
     */
    @ResponseBody
    @PatchMapping("/pwd/{userIdx}") //
    public BaseResponse<String> modifyUserStatus(@PathVariable("userIdx") int userIdx, @RequestBody User.PwdModify pwdModify){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }  // 이 부분까지는 유저가 사용하는 기능 중 유저에 대한 보안이 철저히 필요한 api 에서 사용

            User.PatchPwdReq patchPwdReq = new User.PatchPwdReq(userIdx, pwdModify.getPassword());
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
    public BaseResponse<String> modifyUserStatus(@PathVariable("userIdx") int userIdx, @RequestBody User.Status status){
        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }  // 이 부분까지는 유저가 사용하는 기능 중 유저에 대한 보안이 철저히 필요한 api 에서 사용

            // body,파라미터 유저 인덱스 검증
            if (userIdx != status.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_IDX);
            }

            User.StatusReq statusReq = new User.StatusReq(userIdx, status.getStatus());
            userService.modifyUserStatus(statusReq);

            String result = "탈퇴가 완료되었습니다.";
            return new BaseResponse<>(result);

        } catch (BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 이미지 변경
     */

    @ResponseBody
    @PatchMapping("/profiles/{userIdx}")
    public BaseResponse<String> modifyUserImage(@PathVariable ("userIdx") int userIdx, @RequestBody User.PatchImage patchImage){

       try {
           //jwt에서 idx 추출.

           int userIdxByJwt = jwtService.getUserIdx();
           //userIdx와 접근한 유저가 같은지 확인
           if (userIdx != userIdxByJwt) {
               return new BaseResponse<>(INVALID_USER_JWT);
           }

           // body의 유저인덱스와 파라미터 유저 인덱스 검증.
           if (userIdx != patchImage.getUserIdx()){
               return new BaseResponse<>(INVALID_USER_IDX);
           }

           userService.modifyUserImage(patchImage);
           String result = "프로필 이미지가 변경되었습니다.";

           return new BaseResponse<>(result);
       }catch (BaseException exception) {
           exception.printStackTrace();
           return new BaseResponse<>(exception.getStatus());
       }
    }

    /**
     * 계정 공개,비공개 설정
     */

    @ResponseBody
    @PatchMapping("/private/{userIdx}")
    public BaseResponse<String> modifyUserIsPrivate(@PathVariable ("userIdx") int userIdx,@RequestBody User.PatchPrivate patchPrivate){

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            // body,파라미터 유저 인덱스 검증
            if (userIdx != patchPrivate.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_IDX);
            }

            userService.modifyUserIsPrivate(patchPrivate);

            String result;
            if(patchPrivate.getIsPrivate() == 0){
               result = "공개 계정";
            }
            else
                result = "비공개 계정";

            return new BaseResponse<>(result);

        }catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 전문분야 조회
     */

    @ResponseBody
    @GetMapping("/profiles/specialty/{userIdx}")
    public BaseResponse<GetSpecialtyRes> getSpecialty(@PathVariable ("userIdx") int userIdx){

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetSpecialtyRes getSpecialtyRes = userProvider.getSpecialty(userIdx);
            return new BaseResponse<>(getSpecialtyRes);
        }catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 전문분야 작성(생성)
     */

    @ResponseBody
    @PostMapping("/profiles/specialty/{userIdx}")
    public BaseResponse<String> createSpecialty(@PathVariable ("userIdx") int userIdx, @RequestBody User.SpecialtyReq specialtyReq){

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            int specialtyIdx = userService.createSpecialty(specialtyReq);
            String result = "specialtyIdx = " + specialtyIdx;
            return new BaseResponse<>(result);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 구직 여부 설정
     */

    @ResponseBody
    @PatchMapping("/profiles/jobs/status/{userIdx}")
    public BaseResponse<String> modifyJobSearchStatus(@PathVariable("userIdx") int userIdx, @RequestBody PatchJobsStatusReq patchJobSearchReq){

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // 구직 여부 설정 값은 0.1.2 만 존재.
            if(patchJobSearchReq.getIsJobSearch()>2 || patchJobSearchReq.getIsJobSearch()<0){
                return new BaseResponse<>(INVALID_JOBSEARCH);
            }
            userService.modifyJobSearchStatus(patchJobSearchReq);
            String result = "구직 여부 설정이 완료되었습니다.";
            return new BaseResponse<>(result);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
