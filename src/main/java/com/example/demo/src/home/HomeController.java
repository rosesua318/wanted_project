package com.example.demo.src.home;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.home.model.GetHomeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private final HomeProvider homeProvider;

    public HomeController(HomeProvider homeprovider) {
        this.homeProvider = homeprovider;
    }

    /**
     *홈 화면 조회API
     * [GET] /home/:userIdx/:homecategoryIdx
     *@returnBaseResponse<GetHomeRes>
     */
    @ResponseBody
    @GetMapping()
    public BaseResponse<GetHomeRes> getHome(@RequestParam(value = "homecategoryIdx", defaultValue = "1") int homecategoryIdx) {
        try {
            GetHomeRes getHomeRes = homeProvider.getHome(homecategoryIdx);
            return new BaseResponse<>(getHomeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
