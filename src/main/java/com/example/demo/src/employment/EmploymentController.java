package com.example.demo.src.employment;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.employment.model.GetEmpHomeRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employments")
public class EmploymentController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final EmploymentProvider employmentProvider;

    @Autowired
    private final EmploymentDao employmentDao;

    @Autowired
    private final EmploymentService employmentService;

    @Autowired
    private final JwtService jwtService;

    public EmploymentController(EmploymentProvider employmentProvider, EmploymentDao employmentDao, EmploymentService employmentService, JwtService jwtService) {
        this.employmentProvider = employmentProvider;
        this.employmentDao = employmentDao;
        this.employmentService = employmentService;
        this.jwtService = jwtService;
    }

    /**
     * 비회원일 시에 채용 페이지 화면 조회
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<GetEmpHomeRes> getEmpHome() {

        try{
            GetEmpHomeRes getEmpHomeRes = employmentProvider.getEmpHome();
            return new BaseResponse<>(getEmpHomeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원 일 시 채용 화면 조회
     * @return
     */

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetEmpHomeRes> getEmpHome(@PathVariable("userIdx") int userIdx) {

        try{
            GetEmpHomeRes getEmpHomeRes = employmentProvider.getEmpHome(userIdx);
            return new BaseResponse<>(getEmpHomeRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


}
