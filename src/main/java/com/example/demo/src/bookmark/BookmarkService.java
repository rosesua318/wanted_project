package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.model.PatchBookmarkStatusReq;
import com.example.demo.src.bookmark.model.PostBookMarkReq;
import com.example.demo.src.bookmark.model.PostBookmarkRes;
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

    public PostBookmarkRes createBookmark(PostBookMarkReq postBookMarkReq) throws BaseException {

        // validation : 이미 동일한 북마크 가 있을 경우.
        if(bookmarkProvider.checkBookmark(postBookMarkReq.getEmploymentIdx()) == 1){
            throw new BaseException(POST_BOOKMARK_EXISTS_EMPLOYMENT);
        }

        try{
            int bookmarkIdx = bookmarkDao.createBookmark(postBookMarkReq);

            return new PostBookmarkRes(bookmarkIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void DeleteBookmark(PatchBookmarkStatusReq patchBookmarkStatusReq) throws BaseException{
        try{
            int result = bookmarkDao.modifyBookmarkStatus(patchBookmarkStatusReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_BOOKMARK_STATUS);
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }



}
