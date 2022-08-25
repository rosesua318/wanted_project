package com.example.demo.src.community;

import com.example.demo.src.community.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class CommunityDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetOtherOpenRes getOtherTabOpen(int ctIdx) {
        String getTagQuery = "select ctIdx, name from CommunityTag";

        List<CommunityTag> communityTags = this.jdbcTemplate.query(getTagQuery,
                (rs, rowNum) -> new CommunityTag(
                        rs.getInt("ctIdx"),
                        rs.getString("name")));

        String getPostingQuery = "select p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, ec.category as job, " +
                "case when s.career " +
                "= 0 then '신입' else concat(s.career, '년차') end as career, " +
                "case when timestampdiff(hour, p.createdAt, now()) < 1 then concat(timestampdiff(minute, p.createdAt, now()), '분 전') " +
                "else case when datediff(now(), p.createdAt) >= 1 then date_format(p.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, p.createdAt, now()), '시간 전') end end as date, p.title, p.content, p.imageUrl, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx) as likeNum, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx) as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = ? " +
                "ORDER BY p.createdAt DESC";
        String getPostingParams = String.valueOf(ctIdx);
        List<Posting> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new Posting(
                        rs.getInt("postingIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("job"),
                        rs.getString("career"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imageUrl"),
                        0,
                        rs.getInt("likeNum"),
                        0,
                        rs.getInt("commentNum")
                        ), getPostingParams);



        return new GetOtherOpenRes(ctIdx, communityTags, postingList);
    }

    public GetOtherRes getOtherTab(int userIdx, int ctIdx) {
        String getUserQuery = "select u.userIdx, u.imageUrl, case when u.isNickname = 0 then u.name else u.nickname end as myName, " +
                "ec.category as myJob, case when s.career = 0 then '신입' else concat(s.career, '년차') end as myCareer " +
                "from User u JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where u.userIdx = ?";
        String getUserParams = String.valueOf(userIdx);

        MyUser user = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new MyUser(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("myName"),
                        rs.getString("myJob"),
                        rs.getString("myCareer")
                ), getUserParams);

        String getTagQuery = "select ctIdx, name from CommunityTag";

        List<CommunityTag> communityTags = this.jdbcTemplate.query(getTagQuery,
                (rs, rowNum) -> new CommunityTag(
                        rs.getInt("ctIdx"),
                        rs.getString("name")));

        String getPostingQuery = "select p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, ec.category as job, " +
                "case when s.career " +
                "= 0 then '신입' else concat(s.career, '년차') end as career, " +
                "case when timestampdiff(hour, p.createdAt, now()) < 1 then concat(timestampdiff(minute, p.createdAt, now()), '분 전') " +
                "else case when datediff(now(), p.createdAt) >= 1 then date_format(p.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, p.createdAt, now()), '시간 전') end end as date, p.title, p.content, p.imageUrl, " +
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?) as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx) as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?) as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx) as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = ? " +
                "ORDER BY p.createdAt DESC";
        Object[] getPostingParams = new Object[]{userIdx, userIdx, ctIdx};
        List<Posting> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new Posting(
                        rs.getInt("postingIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("job"),
                        rs.getString("career"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imageUrl"),
                        rs.getInt("isLike"),
                        rs.getInt("likeNum"),
                        rs.getInt("isComment"),
                        rs.getInt("commentNum")
                ), getPostingParams);



        return new GetOtherRes(user, ctIdx, communityTags, postingList);
    }
}
