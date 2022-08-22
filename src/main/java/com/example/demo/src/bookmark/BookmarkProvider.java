package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BookmarkProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookmarkDao bookmarkDao;

    private final JwtService jwtService;

    @Autowired
    public BookmarkProvider(BookmarkDao bookmarkDao, JwtService jwtService) {
        this.bookmarkDao = bookmarkDao;
        this.jwtService = jwtService;
    }


    // 북마크 중복체크
    public int checkBookmark(int employmentIdx) throws BaseException{
        try{
            return bookmarkDao.checkBookmark(employmentIdx);
        }catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
