package com.example.demo.src.company;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.company.model.Company;


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

    public CompanyController(CompanyProvider companyProvider) {
        this.companyProvider = companyProvider;
    }

    /*
    회사 상세 페이지 조회(비회원용) -> 북마크.팔로우 여부 삭제, 연봉 직원 수 비공개
     */
    @ResponseBody
    @GetMapping("/{companyIdx}")
    public BaseResponse<Company.DetailRes> getCompanyDetail(@PathVariable int companyIdx) throws BaseException{

        try{
            Company.DetailRes detailRes = companyProvider.getCompanyDetail(companyIdx);
            return new BaseResponse<>(detailRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    회사 상세 페이지 조회(회원용)
     */

    @ResponseBody
    @GetMapping("/{userIdx}/{companyIdx}")
    public BaseResponse<Company.DetailRes> getCompanyDetail(@PathVariable("userIdx") int userIdx, @PathVariable("companyIdx") int companyIdx) throws BaseException{

        try{
            Company.DetailRes detailRes = companyProvider.getCompanyDetail(userIdx,companyIdx);
            return new BaseResponse<>(detailRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 회사 뉴스 조회 */

    @ResponseBody
    @GetMapping("/news/{companyIdx}/{newsIdx}")
    public BaseResponse<Company.NewsRes> getCompanyNews(@PathVariable("companyIdx") int companyIdx, @PathVariable("newsIdx") int newsIdx) throws BaseException{

        try{
            Company.NewsRes newsRes = companyProvider.getCompanyNews(companyIdx,newsIdx);
            return new BaseResponse<>(newsRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}


