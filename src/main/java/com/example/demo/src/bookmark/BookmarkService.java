package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.model.Bookmark;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BookmarkService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BookmarkDao bookmarkDao;
    private final BookmarkProvider bookmarkProvider;
    private final JwtService jwtService;

    @Autowired
    public BookmarkService(BookmarkDao bookmarkDao, BookmarkProvider bookmarkProvider, JwtService jwtService) {
        this.bookmarkDao = bookmarkDao;
        this.bookmarkProvider = bookmarkProvider;
        this.jwtService = jwtService;
    }

    public void createBookmark(Bookmark.Request request) throws BaseException {

        // 1. 북마크가 이미 등록되어있는 지 확인
        if (bookmarkProvider.checkBookmark(request.getEmploymentIdx(), request.getUserIdx()) == 1) {
            throw new BaseException(POST_BOOKMARK_EXISTS_EMPLOYMENT);
        }
            try {

                //2. 북마크가 DELETE 상태라면 상태만 변경한다.
                if(bookmarkProvider.checkBookmarkDelete(request.getEmploymentIdx()) == 1) {
                    bookmarkDao.ActiveBookmarkStatus(request.getEmploymentIdx(), request.getUserIdx());
                } else {
                    // 3. 북마크 데이터를 새로 생성한다.
                    bookmarkDao.createBookmark(request);
                }
            } catch (Exception exception) {
                throw new BaseException(DATABASE_ERROR);
            }
        }


    public void DeleteBookmark(Bookmark.BookmarkStatus bookmarkStatusReq, int userIdx) throws BaseException{
        try{
            int result = bookmarkDao.DeleteBookmarkStatus(bookmarkStatusReq.getEmploymentIdx(), userIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_BOOKMARK_STATUS);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
