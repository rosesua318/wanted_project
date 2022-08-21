package com.example.demo.src.home;

import com.example.demo.src.home.model.Content;
import com.example.demo.src.home.model.GetHomeRes;
import com.example.demo.src.home.model.HomeCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class HomeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetHomeRes getHome(int homecategoryIdx) {
        String getBannerQuery = "select imageUrl from Banner where type = 0";
        List<String> banners = this.jdbcTemplate.query(getBannerQuery,
                (rs, rowNum) -> new String(
                        rs.getString("imageUrl")));

        String getCategoryQuery = "select homecategoryIdx, homecategory from HomeCategory";
        List<HomeCategory> categories = this.jdbcTemplate.query(getCategoryQuery,
                (rs, rowNum) -> new HomeCategory(
                        rs.getInt("homecategoryIdx"),
                        rs.getString("homecategory")));

        String getContentsQuery = "select c.contentIdx, c.imageUrl, c.title, c.contentUrl, c.content, c.creator, c.creatorImg" +
                " from Content c join HomeCategory hc on c.homecategoryIdx = hc.homecategoryIdx where hc.homecategoryIdx = ?";
        String getContentsParams = String.valueOf(homecategoryIdx);
        List<Content> contents = this.jdbcTemplate.query(getContentsQuery,
                (rs, rowNum) -> new Content(
                        rs.getInt("contentIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("title"),
                        rs.getString("contentUrl"),
                        rs.getString("content"),
                        rs.getString("creator"),
                        rs.getString("creatorImg")),
                getContentsParams);

        return new GetHomeRes(banners, categories, contents);
    }
}
