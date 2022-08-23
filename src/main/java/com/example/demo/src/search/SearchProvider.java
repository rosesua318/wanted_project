package com.example.demo.src.search;

import com.example.demo.config.BaseException;
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

    public List<SearchTag> getRecommTag() throws BaseException {
        try {
            return searchDao.getRecommTag();
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
}
