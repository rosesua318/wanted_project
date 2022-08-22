package com.example.demo.src.employment;

import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.BookmarkDao;
import com.example.demo.src.employment.model.GetEmpDetailRes;
import com.example.demo.src.employment.model.GetEmpHomeRes;
import com.example.demo.src.employment.model.GetEmploymentInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class EmploymentProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmploymentDao employmentDao;

    private final JwtService jwtService;

    public EmploymentProvider(EmploymentDao employmentDao, JwtService jwtService) {
        this.employmentDao = employmentDao;
        this.jwtService = jwtService;
    }

// 채용 정보 화면 조회 ( 상세화면 X)
    public List<GetEmploymentInfoRes> getEmploymentInfoList(int userIdx) throws BaseException {
        try {
            List<GetEmploymentInfoRes> getEmploymentInfoRes = employmentDao.getEmploymentInfo(userIdx);
            return getEmploymentInfoRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }

    }


    // 채용 홈 화면(비회원)

    public GetEmpHomeRes getEmpHome() throws BaseException{
        try{
            GetEmpHomeRes getEmpHomeRes = employmentDao.getEmpHome();
            return getEmpHomeRes;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 채용 홈 화면(회원)
    public GetEmpHomeRes getEmpHome(int userIdx) throws BaseException{
        try{
            GetEmpHomeRes getEmpHomeRes = employmentDao.getEmpHome(userIdx);
            return getEmpHomeRes;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


    // 채용 포지션 상세 페이지
//
//    public GetEmpDetailRes getEmpDetail(int userIdx, int employmentIdx) throws BaseException{
//
//        try{
//            GetEmpDetailRes getEmpDetailRes = employmentDao.getEmpDetail(userIdx,employmentIdx);
//            return getEmpDetailRes;
//        }catch(Exception e){
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//    }
}