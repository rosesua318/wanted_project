package com.example.demo.src.employment;

import com.example.demo.config.BaseException;
import com.example.demo.src.employment.model.Employment;
import com.example.demo.src.employment.model.GetEmploymentInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public Employment.HomeRes getEmpHome() throws BaseException{
        try{
            Employment.HomeRes homeRes = employmentDao.getEmpHome();

            return homeRes;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }

    // 채용 홈 화면(회원)
    public Employment.HomeRes getEmpHome(int userIdx) throws BaseException{
        try{
            Employment.HomeRes homeRes = employmentDao.getEmpHome(userIdx);
            return homeRes;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }


 //    채용 포지션 상세 페이지

    public Employment.DetailRes getEmpDetail(int userIdx, int employmentIdx) throws BaseException{

        try{
            Employment.DetailRes detailRes = employmentDao.getEmpDetail(userIdx,employmentIdx);
            return detailRes;
        }catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }

    }
}