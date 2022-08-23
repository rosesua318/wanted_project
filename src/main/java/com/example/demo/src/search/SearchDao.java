package com.example.demo.src.search;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetRewardRes;
import com.example.demo.src.position.model.Company;
import com.example.demo.src.position.model.EmpRegion;
import com.example.demo.src.position.model.Employment;
import com.example.demo.src.position.model.SearchCategory;
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

    public int checkSearchRecords(int userIdx, int searchIdx) {
        String checkQuery = "select exists(select searchIdx from Search where userIdx = ? and searchIdx = ?)";
        Object[] checkParams = new Object[]{userIdx, searchIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int checkSearchRecordKeyword(int userIdx, String keyword) {
        String checkQuery = "select exists(select searchIdx from Search where userIdx = ? and content LIKE ?)";
        Object[] checkParams = new Object[]{userIdx, keyword};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public void modifySearchRecord(int userIdx, String keyword) {
        String modifyQuery = "update Search set status = 'ACTIVE' where userIdx = ? and content LIKE ?";
        Object[] modifyParams = new Object[]{userIdx, keyword};
        this.jdbcTemplate.update(modifyQuery,modifyParams);
    }

    public void createSearchRecord(int userIdx, String keyword) {
        String createQuery = "insert into Search (userIdx, content) VALUES(?,?)";
        Object[] createParams = new Object[]{userIdx, keyword};
        this.jdbcTemplate.update(createQuery,createParams);
    }

    public void deleteSearchRecords(int userIdx, int searchIdx) {
        String deleteQuery = "update Search set status = 'INACTIVE' where userIdx=? and searchIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, searchIdx};
        this.jdbcTemplate.update(deleteQuery, deleteParams);
    }

    public PostSearchRes searchKeywordOpen(PostSearchReq postSearchReq) {
        String getDutyQuery = "select subcategoryIdx, subcategory from EmploymentSubCategory where replace(subcategory, ' ', '') like ?";
        String getDutyParams = "%" + postSearchReq.getKeyword().replace(" ", "") + "%";
        List<RelatedDuty> duties = this.jdbcTemplate.query(getDutyQuery,
                (rs, rowNum) -> new RelatedDuty(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                " where replace(c.companyName, ' ', '') LIKE ?";
        String getCompanyParams = "%" + postSearchReq.getKeyword().replace(" ", "") + "%";
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);
        if(companies.size() == 0) {
            getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c ORDER BY RAND() LIMIT 12";
            companies = this.jdbcTemplate.query(getCompanyQuery,
                    (rs, rowNum) -> new SearchTagCompany(
                            rs.getInt("companyIdx"),
                            rs.getString("logoUrl"),
                            rs.getString("companyName")));
        }

        for(SearchTagCompany s : companies) {
            s.setIsFollow(0);

            String getTagQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=? LIMIT 1";
            String getTagParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag").replace("#", "")),
                    getTagParams);

            s.setTagList(tags);

        }

        EmpRegion empRegion = new EmpRegion(5, "한국", 5, "전국");

        String getSearchCategory = "select searchcategoryIdx, searchcategory from SearchCategory";
        List<SearchCategory> searchCategories = this.jdbcTemplate.query(getSearchCategory,
                (rs, rowNum) -> new SearchCategory(
                        rs.getInt("searchcategoryIdx"),
                        rs.getString("searchcategory")));


        String getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation from Employment e" +
                " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                "WHERE replace(e.employment, ' ', '') LIKE ?";
        String getEmploymentParams = "%" + postSearchReq.getKeyword().replace(" ", "") + "%";

        List<Employment> employments = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation")),
                getEmploymentParams);

        if(employments.size() == 0) {
            getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation from Employment e" +
                    " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                    "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                    "ORDER BY RAND() LIMIT 12";
            employments = this.jdbcTemplate.query(getEmploymentQuery,
                    (rs, rowNum) -> new Employment(
                            rs.getInt("employmentIdx"),
                            rs.getString("employment"),
                            rs.getString("companyName"),
                            rs.getString("region"),
                            rs.getString("nation")));
        }

        for(Employment e : employments) {
            String getThumbnailQuery = "select employmentImg from EmploymentImg where employmentIdx=?";
            String getThumbnailParams = String.valueOf(e.getEmploymentIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("employmentImg")),
                    getThumbnailParams);

            if(thumbnails.size() != 0) {
                e.setThumbnail(thumbnails.get(0));
            }

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(e.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            e.setReward("채용보상금 " + String.valueOf(reward) + "원");

            e.setIsBookmark(0);
        }


        return new PostSearchRes(postSearchReq.getKeyword(), duties, companies, empRegion, "전체", searchCategories, employments);
    }

    public PostSearchRes searchKeyword(int userIdx, PostSearchReq postSearchReq) {

        String getDutyQuery = "select subcategoryIdx, subcategory from EmploymentSubCategory where replace(subcategory, ' ', '') like ?";
        String getDutyParams = "%" + postSearchReq.getKeyword().replace(" ", "") + "%";
        List<RelatedDuty> duties = this.jdbcTemplate.query(getDutyQuery,
                (rs, rowNum) -> new RelatedDuty(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c " +
                " where replace(c.companyName, ' ', '') LIKE ?";
        String getCompanyParams = "%" + postSearchReq.getKeyword().replace(" ", "") + "%";
        List<SearchTagCompany> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new SearchTagCompany(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                getCompanyParams);
        if(companies.size() == 0) {
            getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c ORDER BY RAND() LIMIT 12";
            companies = this.jdbcTemplate.query(getCompanyQuery,
                    (rs, rowNum) -> new SearchTagCompany(
                            rs.getInt("companyIdx"),
                            rs.getString("logoUrl"),
                            rs.getString("companyName")));
        }

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

            String getTagQuery = "select t.tag from Company c " +
                    "JOIN CompanyTag ct ON ct.companyIdx = c.companyIdx " +
                    "JOIN Tag t ON t.tagIdx = ct.tagIdx where c.companyIdx=? LIMIT 1";
            String getTagParams = String.valueOf(s.getCompanyIdx());
            List<String> tags = this.jdbcTemplate.query(getTagQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("tag").replace("#", "")),
                    getTagParams);

            s.setTagList(tags);

        }

        EmpRegion empRegion = new EmpRegion(5, "한국", 5, "전국");

        String getSearchCategory = "select searchcategoryIdx, searchcategory from SearchCategory";
        List<SearchCategory> searchCategories = this.jdbcTemplate.query(getSearchCategory,
                (rs, rowNum) -> new SearchCategory(
                        rs.getInt("searchcategoryIdx"),
                        rs.getString("searchcategory")));


        String getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation, " +
                "case when(select b.bookmarkIdx from Bookmark b where b.employmentIdx = e.employmentIdx and b.userIdx = ?) is not null then 1 else 0 end as isBookmark from Employment e" +
                " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                "WHERE replace(e.employment, ' ', '') LIKE ?";
        Object[] getEmploymentParams = new Object[]{userIdx, "%" + postSearchReq.getKeyword().replace(" ", "") + "%"};

        List<Employment> employments = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation"),
                        rs.getInt("isBookmark")),
                getEmploymentParams);

        if(employments.size() == 0) {
            getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation, " +
                    "case when(select b.bookmarkIdx from Bookmark b where b.employmentIdx = e.employmentIdx and b.userIdx = ?) is not null then 1 else 0 end as isBookmark from Employment e" +
                    " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                    "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                    "ORDER BY RAND() LIMIT 12";
            String getEmpParams = String.valueOf(userIdx);
            employments = this.jdbcTemplate.query(getEmploymentQuery,
                    (rs, rowNum) -> new Employment(
                            rs.getInt("employmentIdx"),
                            rs.getString("employment"),
                            rs.getString("companyName"),
                            rs.getString("region"),
                            rs.getString("nation"),
                            rs.getInt("isBookmark")),
                    getEmpParams);
        }

        for(Employment e : employments) {
            String getThumbnailQuery = "select employmentImg from EmploymentImg where employmentIdx=?";
            String getThumbnailParams = String.valueOf(e.getEmploymentIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("employmentImg")),
                    getThumbnailParams);

            if(thumbnails.size() != 0) {
                e.setThumbnail(thumbnails.get(0));
            }

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(e.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            e.setReward("채용보상금 " + String.valueOf(reward) + "원");
        }


        return new PostSearchRes(postSearchReq.getKeyword(), duties, companies, empRegion, "전체", searchCategories, employments);
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

    public GetSearchRes getSearchRes(int userIdx) {
        String getRecommTagQuery = "select tagIdx, tag from Tag ORDER BY RAND() LIMIT 5";
        List<SearchTag> recommTags = this.jdbcTemplate.query(getRecommTagQuery,
                (rs, rowNum) -> new SearchTag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")));

        String getSearchRecordQuery = "select searchIdx, content from Search where userIdx=? and status='ACTIVE'";
        String getSearchRecordParams = String.valueOf(userIdx);
        List<SearchRecord> searchRecords = this.jdbcTemplate.query(getSearchRecordQuery,
                (rs, rowNum) -> new SearchRecord(
                        rs.getInt("searchIdx"),
                        rs.getString("content")),
                getSearchRecordParams);

        return new GetSearchRes(recommTags, searchRecords);
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
