package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.PostSearchReq;
import com.example.demo.src.search.model.PostSearchRes;
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

    public void deleteSearchRecords(int userIdx, int searchIdx) throws BaseException {
        try {
            searchDao.deleteSearchRecords(userIdx, searchIdx);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void createSearchRecord(int userIdx, String keyword) throws BaseException {
        try {
            searchDao.createSearchRecord(userIdx, keyword);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifySearchRecord(int userIdx, String keyword) throws BaseException {
        try {
            searchDao.modifySearchRecord(userIdx, keyword);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchRes searchKeywordOpen(PostSearchReq postSearchReq) throws BaseException {
        try {
            return searchDao.searchKeywordOpen(postSearchReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchRes searchKeyword(int userIdx, PostSearchReq postSearchReq) throws BaseException {
        try {
            return searchDao.searchKeyword(userIdx, postSearchReq);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
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
