package com.example.demo.src.company;

import com.example.demo.src.company.model.*;
import com.example.demo.src.employment.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CompanyDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    /* 회사 상세 페이지 조회(비회원용) */
    public GetCompanyDetailRes getCompanyDetail (int companyIdx){

        // 1. CompanyBasic
        String getCompanyBasicQuery = "SELECT logoUrl,companyName,salary,employee FROM Company WHERE Company.companyIdx = ?";

        int getCompanyDetailParams = companyIdx;

        CompanyBasic companyBasic = this.jdbcTemplate.queryForObject(getCompanyBasicQuery,
                (rs, rowNum) -> new CompanyBasic(
                        rs.getString("logoUrl"),
                        rs.getString("companyName")),
                        getCompanyDetailParams);

        // 2. CompanyEmp

        String getCompanyEmpQuery = "SELECT employmentIdx,employment,CONCAT('채용보상금 ',(recommender+applicant),'원') AS compensation, deadline\n" +
                " FROM Employment\n" +
                " JOIN Company ON Company.companyIdx = Employment.companyIdx WHERE Company.CompanyIdx = ? LIMIT 4";

        List<CompanyEmp> companyEmpList = this.jdbcTemplate.query(getCompanyEmpQuery,
                (rs, rowNum) -> new CompanyEmp(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("compensation"),
                        rs.getString("deadline")),
                getCompanyDetailParams);

        // 3. CompanyImg

        String getCompanyImgQuery = "SELECT CI.companyImgIdx, companyImg FROM CompanyImg AS CI\n" +
                "WHERE CI.companyIdx = ?";

        List<CompanyImg> companyImgList = this.jdbcTemplate.query(getCompanyImgQuery,
                (rs,rowNum)-> new CompanyImg(
                        rs.getInt("companyImgIdx"),
                        rs.getString("companyImg")),
                companyIdx);

        // 4. CompanyNews
        String getCompanyNewsQuery = "SELECT CompanyNewsId,News, DATE_FORMAT(createdAt,'%Y-%m-%d') AS createdAt FROM CompanyNews AS CN WHERE CN.companyIdx = ? LIMIT 4";

        List<CompanyNews> companyNewsList = this.jdbcTemplate.query(getCompanyNewsQuery,
                (rs, rowNum) -> new CompanyNews(
                        rs.getInt("CompanyNewsId"),
                        rs.getString("News"),
                        rs.getString("createdAt")),
                companyIdx);

        // 5. Tag

        String getTagQuery = "SELECT T.tagIdx, T.tag FROM Tag AS T\n" +
                "JOIN CompanyTag CT on T.tagIdx = CT.tagIdx WHERE CT.companyIdx = ?";

        List<Tag> tagList = this.jdbcTemplate.query(getTagQuery,
                (rs,rowNum) -> new Tag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                companyIdx);

        return new GetCompanyDetailRes(companyBasic,companyEmpList,companyImgList,companyNewsList,tagList);
    }



    /*
    회사 상세 페이지 조회(회원용)
     */
    public GetCompanyDetailRes getCompanyDetail (int userIdx, int companyIdx){

        // 1. CompanyBasic
        String getCompanyBasicQuery = "SELECT logoUrl,companyName,salary,employee,\n" +
                "CASE WHEN(SELECT Follow.followIdx FROM Follow WHERE Follow.userIdx = ? AND Follow.companyIdx = Company.companyIdx) IS NOT NULL THEN 1 ELSE 0 END AS isFollow FROM Company WHERE Company.companyIdx = ?;";

        Object[] getCompanyDetailParams = new Object[]{userIdx,companyIdx};

        CompanyBasic companyBasic = this.jdbcTemplate.queryForObject(getCompanyBasicQuery,
                (rs, rowNum) -> new CompanyBasic(
                        rs.getString("logoUrl"),
                        rs.getString("companyName"),
                        rs.getString("salary"),
                        rs.getString("employee"),
                        rs.getInt("isFollow")),
                getCompanyDetailParams);

        // 2. CompanyEmp

        String getCompanyEmpQuery = "SELECT employmentIdx,employment,CONCAT('채용보상금 ',(recommender+applicant),'원') AS compensation, deadline,\n" +
                "        CASE WHEN(SELECT Bookmark.bookmarkIdx FROM Bookmark WHERE Bookmark.userIdx = ? AND Bookmark.employmentIdx = Employment.employmentIdx) IS NOT NULL THEN 1 ELSE 0 END AS isBookmark FROM Employment\n" +
                "    JOIN Company ON Company.companyIdx = Employment.companyIdx WHERE Company.CompanyIdx = ? LIMIT 4";

        List<CompanyEmp> companyEmpList = this.jdbcTemplate.query(getCompanyEmpQuery,
                (rs, rowNum) -> new CompanyEmp(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("compensation"),
                        rs.getString("deadline"),
                        rs.getInt("isBookmark")),
                getCompanyDetailParams);

        // 3. CompanyImg

        String getCompanyImgQuery = "SELECT CI.companyImgIdx, companyImg FROM CompanyImg AS CI\n" +
                "WHERE CI.companyIdx = ?";

        List<CompanyImg> companyImgList = this.jdbcTemplate.query(getCompanyImgQuery,
                (rs,rowNum)-> new CompanyImg(
                        rs.getInt("companyImgIdx"),
                        rs.getString("companyImg")),
                companyIdx);

        // 4. CompanyNews
        String getCompanyNewsQuery = "SELECT CompanyNewsId,News, DATE_FORMAT(createdAt,'%Y-%m-%d') AS createdAt FROM CompanyNews AS CN WHERE CN.companyIdx = ? LIMIT 4";

        List<CompanyNews> companyNewsList = this.jdbcTemplate.query(getCompanyNewsQuery,
                (rs, rowNum) -> new CompanyNews(
                        rs.getInt("CompanyNewsId"),
                        rs.getString("News"),
                        rs.getString("createdAt")),
                companyIdx);

        // 5. Tag

        String getTagQuery = "SELECT T.tagIdx, T.tag FROM Tag AS T\n" +
                "JOIN CompanyTag CT on T.tagIdx = CT.tagIdx WHERE CT.companyIdx = ?";

        List<Tag> tagList = this.jdbcTemplate.query(getTagQuery,
                (rs,rowNum) -> new Tag(
                        rs.getInt("tagIdx"),
                        rs.getString("tag")),
                companyIdx);

        return new GetCompanyDetailRes(companyBasic,companyEmpList,companyImgList,companyNewsList,tagList);
    }
}