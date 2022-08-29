package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.oauth.model.KakaoUserLoginRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/kakao")
public class OauthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final KakaoAPI kakaoAPI;
    @Autowired
    private final KakaoUserService kakaoUserService;

    public OauthController(KakaoAPI kakaoAPI, KakaoUserService kakaoUserService) {
        this.kakaoAPI = kakaoAPI;
        this.kakaoUserService = kakaoUserService;
    }

    @RequestMapping(value = "/callback")
    public BaseResponse<KakaoUserLoginRes> logIn(@RequestParam("code") String code, HttpSession session) {
        try {
            // 1. 인증코드 요청 및 전달
            String accessToken = kakaoAPI.getAccessToken(code);
            // 2. 인증코드로 토큰 전달
            HashMap<String, Object> userInfo = kakaoAPI.getUserInfo(accessToken);

            String nickname = (String) userInfo.get("nickname");
            String email = (String) userInfo.get("email");

            //         System.out.println("login info : " + userInfo.toString());

            logger.info("login info {}", userInfo.toString());

            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_token", accessToken);

            KakaoUserLoginRes kakaoUserLoginRes = kakaoUserService.logIn(nickname, email);
            return new BaseResponse<>(kakaoUserLoginRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
