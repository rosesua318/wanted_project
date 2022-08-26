package com.example.demo.src.employment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.bookmark.BookmarkDao;
import com.example.demo.src.bookmark.BookmarkProvider;
import com.example.demo.src.employment.model.PostApplicationReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;


@Service
public class EmploymentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmploymentDao employmentDao;
    private final EmploymentProvider employmentProvider;
    private final JwtService jwtService;

    @Autowired
    public EmploymentService(EmploymentDao employmentDao, EmploymentProvider employmentProvider, JwtService jwtService) {
        this.employmentDao = employmentDao;
        this.employmentProvider = employmentProvider;
        this.jwtService = jwtService;
    }

    // 지원하기
    public int createApplicant(PostApplicationReq postApplicationReq) throws BaseException{
        try{
            int applicantIdx = employmentDao.createApplicant(postApplicationReq);
            return applicantIdx;
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
