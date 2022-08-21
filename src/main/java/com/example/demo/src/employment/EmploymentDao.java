package com.example.demo.src.employment;

import com.example.demo.src.employment.model.GetEmploymentInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EmploymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 채용 포지션 조회(상세화면 x)
    public List<GetEmploymentInfoRes> getEmploymentInfo (int userIdx){


        String getEmploymentQuery =
                "SELECT employmentImg,employment,companyName,N.name AS nation,CONCAT((applicant+recommender),'원') AS compensation FROM Employment AS EMP "+
                        "JOIN EmploymentImg AS EMPIMG ON EMPIMG.employmentIdx = EMP.employmentIdx "+
                        "JOIN Company AS C ON C.companyIdx = EMP.companyIdx "+
                        "JOIN Nation AS N "+
                        "JOIN EmpRegion AS EMPR ON EMPR.employmentIdx = EMP.employmentIdx AND N.nationIdx = EMPR.nationIdx "+
                        "JOIN Bookmark AS BM ON BM.employmentIdx = EMP.employmentIdx AND BM.status = 'ACTIVE' WHERE BM.userIdx= ?";

        int getEmploymentParams = userIdx;

        return this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new GetEmploymentInfoRes(
                        rs.getString("employmentImg"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("nation"),
                        rs.getString("compensation")),
                getEmploymentParams);
    }

}