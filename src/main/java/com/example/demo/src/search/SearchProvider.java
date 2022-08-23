package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.search.model.GetCompanyTagHomeRes;
import com.example.demo.src.search.model.GetSearchRes;
import com.example.demo.src.search.model.PostSearchTagRes;
import com.example.demo.src.search.model.SearchTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class SearchProvider {
    private final SearchDao searchDao;


    public SearchProvider(SearchDao searchDao) {
        this.searchDao = searchDao;
    }

    public int checkSearchRecords(int userIdx, int searchIdx) throws BaseException{
        try{
            return searchDao.checkSearchRecords(userIdx, searchIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkSearchRecordKeyword(int userIdx, String keyword) throws BaseException{
        try{
            return searchDao.checkSearchRecordKeyword(userIdx, keyword);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<SearchTag> getRecommTag() throws BaseException {
        try {
            return searchDao.getRecommTag();
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetSearchRes getSearchRes(int userIdx) throws BaseException {
        try {
            return searchDao.getSearchRes(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchTagRes searchClickOpen(int tagIdx) throws BaseException {
        try {
            return searchDao.searchClickOpen(tagIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostSearchTagRes searchClick(int tagIdx, int userIdx) throws BaseException {
        try {
            return searchDao.searchClick(tagIdx, userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetCompanyTagHomeRes getCompanyTagHomeOpen() throws BaseException {
        try {
            return searchDao.getCompanyTagHomeOpen();
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetCompanyTagHomeRes getCompanyTagHome(int userIdx) throws BaseException {
        try {
            return searchDao.getCompanyTagHome(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
