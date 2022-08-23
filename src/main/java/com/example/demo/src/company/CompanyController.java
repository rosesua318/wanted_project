package com.example.demo.src.company;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.company.model.GetCompanyDetailRes;
import com.example.demo.src.company.model.GetCompanyNewsRes;
import com.example.demo.src.employment.model.GetEmpHomeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CompanyProvider companyProvider;
    @Autowired
    private final CompanyService companyService;
    @Autowired
    private final CompanyDao companyDao;

    public CompanyController(CompanyProvider companyProvider, CompanyService companyService, CompanyDao companyDao) {
        this.companyProvider = companyProvider;
        this.companyService = companyService;
        this.companyDao = companyDao;
    }

    /*
    회사 상세 페이지 조회(비회원용) -> 북마크.팔로우 여부 삭제, 연봉 직원 수 비공개
     */
    @ResponseBody
    @GetMapping("/{companyIdx}")
    public BaseResponse<GetCompanyDetailRes> getCompanyDetail(@PathVariable int companyIdx) throws BaseException{

        try{

            GetCompanyDetailRes getCompanyDetailRes = companyProvider.getCompanyDetail(companyIdx);

            return new BaseResponse<>(getCompanyDetailRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
//


    /*
    회사 상세 페이지 조회(회원용)
     */

    @ResponseBody
    @GetMapping("/{userIdx}/{companyIdx}")
    public BaseResponse<GetCompanyDetailRes> getCompanyDetail(@PathVariable("userIdx") int userIdx, @PathVariable("companyIdx") int companyIdx) throws BaseException{

        try{

            GetCompanyDetailRes getCompanyDetailRes = companyProvider.getCompanyDetail(userIdx,companyIdx);

            return new BaseResponse<>(getCompanyDetailRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    /* 회사 뉴스 조회 */

    @ResponseBody
    @GetMapping("/news/{companyIdx}/{newsIdx}")
    public BaseResponse<GetCompanyNewsRes> getCompanyNews(@PathVariable("companyIdx") int companyIdx, @PathVariable("newsIdx") int newsIdx) throws BaseException{

        try{

            GetCompanyNewsRes getCompanyNewsRes = companyProvider.getCompanyNews(companyIdx,newsIdx);

            return new BaseResponse<>(getCompanyNewsRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}


