package com.example.demo.src.company;


import com.example.demo.config.BaseException;
import com.example.demo.src.company.model.Company;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Transactional(readOnly = true)
@Service
public class CompanyProvider {

    private final CompanyDao companyDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CompanyProvider(CompanyDao companyDao, JwtService jwtService) {
        this.companyDao = companyDao;
        this.jwtService = jwtService;
    }

    /* 회사 상세 페이지 조회(비회원용) */
    public Company.DetailRes getCompanyDetail(int companyIdx) throws BaseException {

        try {
            Company.DetailRes detailRes = companyDao.getCompanyDetail(companyIdx);
            return detailRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /*
    회사 상사 페이지 조회(회원용)
     */

    public Company.DetailRes getCompanyDetail(int userIdx, int companyIdx) throws BaseException {

        try {
            Company.DetailRes detailRes = companyDao.getCompanyDetail(userIdx, companyIdx);
            return detailRes;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /* 회사 뉴스 조회 */

    public Company.NewsRes getCompanyNews(int companyIdx, int newsIdx) throws BaseException{
        try{
            Company.NewsRes newsRes = companyDao.getCompanyNews(companyIdx,newsIdx);
            return newsRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}