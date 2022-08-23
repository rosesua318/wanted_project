package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.PostSearchTagReq;
import com.example.demo.src.search.model.PostSearchTagRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class SearchService {

    private final SearchDao searchDao;

    public SearchService(SearchDao serviceDao) {
        this.searchDao = serviceDao;
    }

    public PostSearchTagRes searchTagOpen(PostSearchTagReq postSearchTagReq) throws BaseException {
        try {
            return searchDao.searchTagOpen(postSearchTagReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchTagRes searchTag(int userIdx, PostSearchTagReq postSearchTagReq) throws BaseException {
        try {
            return searchDao.searchTag(userIdx, postSearchTagReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
