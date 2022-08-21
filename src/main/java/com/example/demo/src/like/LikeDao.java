package com.example.demo.src.like;

import com.example.demo.src.like.model.GetImgRes;
import com.example.demo.src.like.model.GetLikeRes;
import com.example.demo.src.like.model.GetNationRes;
import com.example.demo.src.like.model.GetRewardRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LikeDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkLikes(int userIdx, int employmentIdx) {
        String checkQuery = "select exists(select likeIdx from LikeEmp where userIdx = ? and employmentIdx = ?)";
        Object[] checkParams = new Object[]{userIdx, employmentIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int modifyLikes(int userIdx, int employmentIdx) {
        String modifyQuery = "update LikeEmp set status = 'ACTIVE' where userIdx=? and employmentIdx = ?";
        Object[] modifyParams = new Object[]{userIdx, employmentIdx};
        this.jdbcTemplate.update(modifyQuery,modifyParams);

        String getQuery = "select likeIdx from LikeEmp where userIdx=? and employmentIdx = ?";
        Object[] getParams = new Object[]{userIdx, employmentIdx};
        return this.jdbcTemplate.queryForObject(getQuery,
                int.class,
                getParams);
    }

    public void deleteLikes(int userIdx, int employmentIdx) {
        String deleteQuery = "update LikeEmp set status = 'INACTIVE' where userIdx=? and employmentIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, employmentIdx};
        this.jdbcTemplate.update(deleteQuery, deleteParams);
    }

    public int createLikes(int userIdx, int employmentIdx) {
        String createQuery = "insert into LikeEmp (userIdx, employmentIdx) VALUES (?,?)";
        Object[] createParams = new Object[]{userIdx, employmentIdx};
        this.jdbcTemplate.update(createQuery,createParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public List<GetLikeRes> getLikes(int userIdx) {
        String getLikesQuery = "select e.employmentIdx, e.employment, c.companyName from Employment e join" +
                " LikeEmp le ON e.employmentIdx = le.employmentIdx JOIN User u ON u.userIdx = le.userIdx " +
                "JOIN Company c ON c.companyIdx = e.companyIdx where u.userIdx =? and le.status='ACTIVE'";
        String getLikesParams = String.valueOf(userIdx);
        List<GetLikeRes> getLikeRes = this.jdbcTemplate.query(getLikesQuery,
                (rs, rowNum) -> new GetLikeRes(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName")),
                getLikesParams);

        for(GetLikeRes g : getLikeRes) {
            String getImgQuery = "select i.employmentImg from Employment e join EmploymentImg i ON e.employmentIdx = i.employmentIdx" +
                    " where e.employmentIdx =?";
            String getImgParams = String.valueOf(g.getEmploymentIdx());
            List<GetImgRes> getImg = this.jdbcTemplate.query(getImgQuery,
                    (rs, rowNum) -> new GetImgRes(
                            rs.getString("employmentImg")),
                    getImgParams);
            g.setEmploymentImg(getImg.get(0).getEmploymentImg());

            String getNationQuery = "select n.name as nation, r.name as region from Employment e join EmpRegion er ON e.employmentIdx = er.employmentIdx" +
                    " join Nation n on er.nationIdx = n.nationIdx join Region r on r.regionIdx = er.regionIdx where e.employmentIdx =?";
            String getNationParams = String.valueOf(g.getEmploymentIdx());
            GetNationRes getNationRes = this.jdbcTemplate.queryForObject(getNationQuery,
                    (rs, rowNum) -> new GetNationRes(
                            rs.getString("nation"),
                            rs.getString("region")),
                    getNationParams);
            g.setNation(getNationRes.getNation());
            g.setRegion(getNationRes.getRegion());

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(g.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            g.setReward("채용보상금 " + String.valueOf(reward) + "원");
        }

        return getLikeRes;
    }
}
