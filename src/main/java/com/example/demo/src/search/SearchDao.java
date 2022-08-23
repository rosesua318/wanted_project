package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.src.position.model.Company;
import com.example.demo.src.search.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.SEARCH_TAG_NO_DATA;

@Repository
public class SearchDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public PostSearchTagRes searchTagOpen(PostSearchTagReq postSearchTagReq) throws BaseException {
        String getSearchTagQuery = "select tagIdx, tag from Tag where tag like ?";
        String getSearchTagParams = postSearchTagReq.getTag();
        SearchTag searchTag;
        try {
            searchTag = this.jdbcTemplate.queryForObject(getSearchTagQuery,
                    (rs, rowNum) -> new SearchTag(
                            rs.getInt("tagIdx"),
                            rs.getString("tag")),
                    getSearchTagParams);
        }  catch (EmptyResultDataAccessException e) {
            throw new BaseException(SEARCH_TAG_NO_DATA);
        }

        String getRecommTagQuery = "select tagIdx, tag from Tag where tagIdx != ? ORDER BY RAND() LIMIT 4";
        String getRecommTagParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                getRecommTagParams);

        recommTags.add(0, searchTag);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx where t.tagIdx=?";
        String getCompanyParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);

        for(SearchTagCompany s : companies) {
            s.setIsFollow(0);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=?";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new PostSearchTagRes(searchTag, recommTags, companies);
    }



    public PostSearchTagRes searchTag(int userIdx, PostSearchTagReq postSearchTagReq) throws BaseException {
        String getSearchTagQuery = "select tagIdx, tag from Tag where tag like ?";
        String getSearchTagParams = postSearchTagReq.getTag();
        SearchTag searchTag;
        try {
            searchTag = this.jdbcTemplate.queryForObject(getSearchTagQuery,
                    (rs, rowNum) -> new SearchTag(
                            rs.getInt("tagIdx"),
                            rs.getString("tag")),
                    getSearchTagParams);
        }  catch (EmptyResultDataAccessException e) {
            throw new BaseException(SEARCH_TAG_NO_DATA);
        }

        String getRecommTagQuery = "select tagIdx, tag from Tag where tagIdx != ? ORDER BY RAND() LIMIT 4";
        String getRecommTagParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                getRecommTagParams);

        recommTags.add(0, searchTag);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx where t.tagIdx=?";
        String getCompanyParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);

        for(SearchTagCompany s : companies) {
            String getFollowQuery = "select case when(select f.followIdx from Follow f where f.companyIdx = c.companyIdx and f.userIdx = ?) " +
                    "is not null then 1 else 0 end as isFollow from Company c where c.companyIdx = ?";
            Object[] getFollowParams = new Object[]{userIdx, s.getCompanyIdx()};
            int isFollow = this.jdbcTemplate.queryForObject(getFollowQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("isFollow")
                    ),
                    getFollowParams);
            s.setIsFollow(isFollow);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=?";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new PostSearchTagRes(searchTag, recommTags, companies);
    }

    public List<SearchTag> getRecommTag() {
        String getRecommTagQuery = "select tagIdx, tag from Tag ORDER BY RAND() LIMIT 5";
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")));

        return recommTags;
    }

    public PostSearchTagRes searchClickOpen(int tagIdx) throws BaseException {
        String getSearchTagQuery = "select tagIdx, tag from Tag where tagIdx=?";
        String getSearchTagParams = String.valueOf(tagIdx);
        SearchTag searchTag;
        try {
            searchTag = this.jdbcTemplate.queryForObject(getSearchTagQuery,
                    (rs, rowNum) -> new SearchTag(
                            rs.getInt("tagIdx"),
                            rs.getString("tag")),
                    getSearchTagParams);
        }  catch (EmptyResultDataAccessException e) {
            throw new BaseException(SEARCH_TAG_NO_DATA);
        }

        String getRecommTagQuery = "select tagIdx, tag from Tag where tagIdx != ? ORDER BY RAND() LIMIT 4";
        String getRecommTagParams = String.valueOf(tagIdx);
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                getRecommTagParams);

        recommTags.add(0, searchTag);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx where t.tagIdx=?";
        String getCompanyParams = String.valueOf(tagIdx);
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);

        for(SearchTagCompany s : companies) {
            s.setIsFollow(0);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=?";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new PostSearchTagRes(searchTag, recommTags, companies);
    }

    public PostSearchTagRes searchClick(int tagIdx, int userIdx) throws BaseException {
        String getSearchTagQuery = "select tagIdx, tag from Tag where tagIdx=?";
        String getSearchTagParams = String.valueOf(tagIdx);
        SearchTag searchTag;
        try {
            searchTag = this.jdbcTemplate.queryForObject(getSearchTagQuery,
                    (rs, rowNum) -> new SearchTag(
                            rs.getInt("tagIdx"),
                            rs.getString("tag")),
                    getSearchTagParams);
        }  catch (EmptyResultDataAccessException e) {
            throw new BaseException(SEARCH_TAG_NO_DATA);
        }

        String getRecommTagQuery = "select tagIdx, tag from Tag where tagIdx != ? ORDER BY RAND() LIMIT 4";
        String getRecommTagParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                getRecommTagParams);

        recommTags.add(0, searchTag);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx where t.tagIdx=?";
        String getCompanyParams = String.valueOf(searchTag.getTagIdx());
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);

        for(SearchTagCompany s : companies) {
            String getFollowQuery = "select case when(select f.followIdx from Follow f where f.companyIdx = c.companyIdx and f.userIdx = ?) " +
                    "is not null then 1 else 0 end as isFollow from Company c where c.companyIdx = ?";
            Object[] getFollowParams = new Object[]{userIdx, s.getCompanyIdx()};
            int isFollow = this.jdbcTemplate.queryForObject(getFollowQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("isFollow")
                    ),
                    getFollowParams);
            s.setIsFollow(isFollow);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=?";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new PostSearchTagRes(searchTag, recommTags, companies);
    }

    public GetCompanyTagHomeRes getCompanyTagHomeOpen() throws BaseException {
        String getRecommTagQuery = "select tagIdx, tag from Tag ORDER BY RAND() LIMIT 5";
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")));

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx ORDER BY RAND() LIMIT 10";
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")));

        for(SearchTagCompany s : companies) {
            s.setIsFollow(0);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=? LIMIT 1";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new GetCompanyTagHomeRes(recommTags, companies);
    }

    public GetCompanyTagHomeRes getCompanyTagHome(int userIdx) throws BaseException {
        String getRecommTagQuery = "select tagIdx, tag from Tag ORDER BY RAND() LIMIT 5";
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")));

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                "JOIN Tag t ON t.tagIdx = ct.tagIdx ORDER BY RAND() LIMIT 10";
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")));

        for(SearchTagCompany s : companies) {
            String getFollowQuery = "select case when(select f.followIdx from Follow f where f.companyIdx = c.companyIdx and f.userIdx = ?) " +
                    "is not null then 1 else 0 end as isFollow from Company c where c.companyIdx = ?";
            Object[] getFollowParams = new Object[]{userIdx, s.getCompanyIdx()};
            int isFollow = this.jdbcTemplate.queryForObject(getFollowQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("isFollow")
                    ),
                    getFollowParams);
            s.setIsFollow(isFollow);

            String getTagListQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=? LIMIT 1";
            String getTagListParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagListQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag")),
                    getTagListParams);

            s.setTagList(tags);

        }

        return new GetCompanyTagHomeRes(recommTags, companies);
    }
}
