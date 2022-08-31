package com.example.demo.src.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;


@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 팔로우 등록여부 체크


    public int checkFollows(int userIdx, int companyIdx){

        String createFollowQuery = "SELECT exists(SELECT followIdx FROM Follow where userIdx = ? AND companyIdx = ?)";

        Object[] checkParams = new Object[]{userIdx,companyIdx};

        return this.jdbcTemplate.queryForObject(createFollowQuery,
                int.class,
                checkParams);
    }


    // 팔로우 등록

    public int createFollow(int userIdx, int companyIdx){
        String createQuery = "INSERT INTO Follow (userIdx, companyIdx) VALUES (?,?)";
        Object[] createParams = new Object[]{userIdx,companyIdx};

        this.jdbcTemplate.update(createQuery,createParams);

        String lastInsertIdQuery = "SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }


    // 팔로우 삭제

    public void deleteFollow(int userIdx, int companyIdx){
        String deleteQuery = "UPDATE Follow SET status = 'DELETE' WHERE userIdx=? AND companyIdx = ?;";
        Object[] deleteParams = new Object[]{userIdx,companyIdx};
        this.jdbcTemplate.update(deleteQuery,deleteParams);
    }
}
