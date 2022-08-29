package com.example.demo.src.oauth;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class KakaoUserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;

        int result = this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
        return result;

    }

    public int createKakaoUser(String nickname, Object email) throws BaseException {

        String createUserQuery = "insert into User (name, email) VALUES (?,?)";
        Object[] createUserParams = new Object[]{nickname, email};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int getEmail(String email) {
        String getEmailQuery = "select userIdx from User where status = 'ACTIVE' AND email = ?";
        String getEmailParams = email;


        int result = this.jdbcTemplate.queryForObject(getEmailQuery,
                int.class,
                getEmailParams);
        return result;
    }
}

