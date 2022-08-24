package com.example.demo.src.application;

import com.example.demo.src.application.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ApplicationDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetApplyRes getApplies(int userIdx) {
        String getApplicationQuery = "select a.state from User u " +
                "JOIN Application a ON a.userIdx = u.userIdx " +
                "where u.userIdx=?";
        String getApplicationParams = String.valueOf(userIdx);
        List<Integer> states = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("state")),
                getApplicationParams);
        ApplicationStatus applicationStatus = new ApplicationStatus(0, 0, 0, 0, 0);
        int total = 0;
        int writing = 0;
        for(int a : states) {
            if(a == 0) {
                writing += 1;
            } else if(a == 1) {
                total += 1;
                int n = applicationStatus.getCompleted();
                n += 1;
                applicationStatus.setCompleted(n);
            } else if (a == 2) {
                total += 1;
                int n = applicationStatus.getBrowse();
                n += 1;
                applicationStatus.setBrowse(n);
            } else if (a == 3) {
                total += 1;
                int n = applicationStatus.getPass();
                n += 1;
                applicationStatus.setPass(n);
            } else if (a == 4) {
                total += 1;
                int n = applicationStatus.getFail();
                n += 1;
                applicationStatus.setFail(n);
            }
        }

        applicationStatus.setTotal(total);

        getApplicationQuery = "select a.applicationIdx, c.companyName, e.employment, date_format(a.createdAt, '%Y.%m.%d') as writeTime, a.state, a.recommend " +
                "FROM User u JOIN Application a ON u.userIdx = a.userIdx " +
                "JOIN Employment e ON e.employmentIdx = a.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.employmentIdx " +
                "where u.userIdx=? and a.state != 0";
        getApplicationParams = String.valueOf(userIdx);
        List<Application> applications = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Application(
                        rs.getInt("applicationIdx"),
                        rs.getString("companyName"),
                        rs.getString("employment"),
                        rs.getString("writeTime"),
                        rs.getString("state"),
                        rs.getString("recommend")),
                getApplicationParams);

        for(Application a : applications) {
            if(a.getState().equals("1")) {
                a.setState("지원 완료");
            } else if(a.getState().equals("2")) {
                a.setState("이력서 열람");
            } else if(a.getState().equals("3")) {
                a.setState("최종 합격");
            } else if(a.getState().equals("4")) {
                a.setState("불합격");
            }

            if(a.getRecommend().equals("0")) {
                a.setRecommend("추천인 없음");
            }
        }


        return new GetApplyRes(writing, total, applicationStatus, applications);
    }


    public GetWritingRes getWritings(int userIdx) {
        String getApplicationQuery = "select a.state from User u " +
                "JOIN Application a ON a.userIdx = u.userIdx " +
                "where u.userIdx=?";
        String getApplicationParams = String.valueOf(userIdx);
        List<Integer> states = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("state")),
                getApplicationParams);
        int total = 0;
        int writing = 0;
        for(int a : states) {
            if(a == 0) {
                writing += 1;
            } else {
                total += 1;
            }
        }


        getApplicationQuery = "select a.applicationIdx, c.companyName, e.employment, date_format(a.createdAt, '%Y.%m.%d') as writeTime, a.state, a.recommend " +
                "FROM User u JOIN Application a ON u.userIdx = a.userIdx " +
                "JOIN Employment e ON e.employmentIdx = a.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.employmentIdx " +
                "where u.userIdx=? and a.state = 0";
        getApplicationParams = String.valueOf(userIdx);
        List<Application> applications = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Application(
                        rs.getInt("applicationIdx"),
                        rs.getString("companyName"),
                        rs.getString("employment"),
                        rs.getString("writeTime"),
                        rs.getString("state"),
                        rs.getString("recommend")),
                getApplicationParams);

        for(Application a : applications) {
            if(a.getState().equals("0")) {
                a.setState("작성 중");
            }
            if(a.getRecommend().equals("0")) {
                a.setRecommend("추천인 없음");
            }
        }


        return new GetWritingRes(writing, total, applications);
    }


    public GetApplyRes searchApplies(int userIdx, PostSearchApplyReq postSearchApplyReq) {
        String getApplicationQuery = "select a.state from User u " +
                "JOIN Application a ON a.userIdx = u.userIdx " +
                "where u.userIdx=?";
        String getApplicationParams = String.valueOf(userIdx);
        List<Integer> states = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("state")),
                getApplicationParams);
        ApplicationStatus applicationStatus = new ApplicationStatus(0, 0, 0, 0, 0);
        int total = 0;
        int writing = 0;
        for(int a : states) {
            if(a == 0) {
                writing += 1;
            } else if(a == 1) {
                total += 1;
                int n = applicationStatus.getCompleted();
                n += 1;
                applicationStatus.setCompleted(n);
            } else if (a == 2) {
                total += 1;
                int n = applicationStatus.getBrowse();
                n += 1;
                applicationStatus.setBrowse(n);
            } else if (a == 3) {
                total += 1;
                int n = applicationStatus.getPass();
                n += 1;
                applicationStatus.setPass(n);
            } else if (a == 4) {
                total += 1;
                int n = applicationStatus.getFail();
                n += 1;
                applicationStatus.setFail(n);
            }
        }

        applicationStatus.setTotal(total);

        getApplicationQuery = "select a.applicationIdx, c.companyName, e.employment, date_format(a.createdAt, '%Y.%m.%d') as writeTime, a.state, a.recommend " +
                "FROM User u JOIN Application a ON u.userIdx = a.userIdx " +
                "JOIN Employment e ON e.employmentIdx = a.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.employmentIdx " +
                "where u.userIdx=? and a.state != 0 and replace(c.companyName, ' ', '') LIKE ?";
        Object[] getApplyParams = new Object[]{userIdx, postSearchApplyReq.getKeyword().replace(" ", "")};
        List<Application> applications = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Application(
                        rs.getInt("applicationIdx"),
                        rs.getString("companyName"),
                        rs.getString("employment"),
                        rs.getString("writeTime"),
                        rs.getString("state"),
                        rs.getString("recommend")),
                getApplyParams);

        for(Application a : applications) {
            if(a.getState().equals("1")) {
                a.setState("지원 완료");
            } else if(a.getState().equals("2")) {
                a.setState("이력서 열람");
            } else if(a.getState().equals("3")) {
                a.setState("최종 합격");
            } else if(a.getState().equals("4")) {
                a.setState("불합격");
            }

            if(a.getRecommend().equals("0")) {
                a.setRecommend("추천인 없음");
            }
        }


        return new GetApplyRes(writing, total, applicationStatus, applications);
    }


    public GetWritingRes searchWritings(int userIdx, PostSearchApplyReq postSearchApplyReq) {
        String getApplicationQuery = "select a.state from User u " +
                "JOIN Application a ON a.userIdx = u.userIdx " +
                "where u.userIdx=?";
        String getApplicationParams = String.valueOf(userIdx);
        List<Integer> states = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("state")),
                getApplicationParams);
        int total = 0;
        int writing = 0;
        for(int a : states) {
            if(a == 0) {
                writing += 1;
            } else {
                total += 1;
            }
        }


        getApplicationQuery = "select a.applicationIdx, c.companyName, e.employment, date_format(a.createdAt, '%Y.%m.%d') as writeTime, a.state, a.recommend " +
                "FROM User u JOIN Application a ON u.userIdx = a.userIdx " +
                "JOIN Employment e ON e.employmentIdx = a.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.employmentIdx " +
                "where u.userIdx=? and a.state = 0 and replace(c.companyName, ' ', '') LIKE ?";
        Object[] getApplyParams = new Object[]{userIdx, postSearchApplyReq.getKeyword().replace(" ", "")};
        List<Application> applications = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Application(
                        rs.getInt("applicationIdx"),
                        rs.getString("companyName"),
                        rs.getString("employment"),
                        rs.getString("writeTime"),
                        rs.getString("state"),
                        rs.getString("recommend")),
                getApplyParams);

        for(Application a : applications) {
            if(a.getState().equals("0")) {
                a.setState("작성 중");
            }
            if(a.getRecommend().equals("0")) {
                a.setRecommend("추천인 없음");
            }
        }


        return new GetWritingRes(writing, total, applications);
    }
}
