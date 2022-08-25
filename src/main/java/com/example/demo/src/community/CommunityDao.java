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


    public GetAllOpenRes getAllTabOpen() {
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
                "where pt.ctIdx = 2 " +
                "ORDER BY p.createdAt DESC";
        List<PostingMore> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new PostingMore(
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
                ));

        for(PostingMore p : postingList) {
            String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
            String getParams = String.valueOf(p.getPostingIdx());
            List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                    (rs, rowNum) -> new CommunityTag(
                            rs.getInt("ctIdx"),
                            rs.getString("name")
                    ), getParams);

            p.setTags(tags);
        }



        return new GetAllOpenRes(2, communityTags, postingList);
    }

    public GetAllRes getAllTab(int userIdx) {
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
                "where pt.ctIdx = 2 " +
                "ORDER BY p.createdAt DESC";
        Object[] getPostingParams = new Object[]{userIdx, userIdx};
        List<PostingMore> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new PostingMore(
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

        for(PostingMore p : postingList) {
            String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
            String getParams = String.valueOf(p.getPostingIdx());
            List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                    (rs, rowNum) -> new CommunityTag(
                            rs.getInt("ctIdx"),
                            rs.getString("name")
                    ), getParams);

            p.setTags(tags);
        }

        return new GetAllRes(user, 2, communityTags, postingList);
    }


    public GetRecommOpenRes getRecommTabOpen() {
        String getTagQuery = "select ctIdx, name from CommunityTag";

        List<CommunityTag> communityTags = this.jdbcTemplate.query(getTagQuery,
                (rs, rowNum) -> new CommunityTag(
                        rs.getInt("ctIdx"),
                        rs.getString("name")));

        String recommTag = "#커리어고민 #취업/이직 #회사생활 #인관관계 #IT/기술 #라이프스타일 #리더십 #조직문화";

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
                "where pt.ctIdx = 2 " +
                "ORDER BY p.createdAt DESC";
        List<PostingMore> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new PostingMore(
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
                ));

        for(PostingMore p : postingList) {
            String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
            String getParams = String.valueOf(p.getPostingIdx());
            List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                    (rs, rowNum) -> new CommunityTag(
                            rs.getInt("ctIdx"),
                            rs.getString("name")
                    ), getParams);

            p.setTags(tags);
        }



        return new GetRecommOpenRes(1, communityTags, recommTag, postingList);
    }

    public GetRecommRes getRecommTab(int userIdx) {
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

        String getRecommQuery = "select concat('#', h.homecategory, ' ') as homecategory " +
                "from HomeCategory h " +
                "JOIN UserInterestTag ut ON ut.homecategoryIdx = h.homecategoryIdx " +
                "JOIN User u ON ut.userIdx = u.userIdx " +
                "where ut.userIdx = ? and ut.status = 'ACTIVE'";
        Object[] getRecommParams = new Object[]{userIdx};
        
        List<String> recomm = this.jdbcTemplate.query(getRecommQuery,
                (rs, rowNum) -> new String(
                        rs.getString("homecategory")
                ), getRecommParams);
        
        String recommTag = "";
        
        for(String r : recomm) {
            recommTag += r;
        }

        String getPostingQuery;
        Object[] getPostingParams;
        List<PostingMore> postingList;
        if(recommTag.equals("")) {
            recommTag = "#커리어고민 #취업/이직 #회사생활 #인관관계 #IT/기술";
            getPostingQuery = "select p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
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
                    "where pt.ctIdx = 2 " +
                    "ORDER BY p.createdAt DESC";

            getPostingParams = new Object[]{userIdx, userIdx};
            postingList = this.jdbcTemplate.query(getPostingQuery,
                    (rs, rowNum) -> new PostingMore(
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
        } else {
            getPostingQuery = "select DISTINCT p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
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
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx " +
                    "where ct.name IN (select h.homecategory from User u JOIN UserInterestTag ut ON u.userIdx = ut.userIdx JOIN HomeCategory h ON h.homecategoryIdx = ut.homecategoryIdx) " +
                    "ORDER BY p.createdAt DESC";
            getPostingParams = new Object[]{userIdx, userIdx};
            postingList = this.jdbcTemplate.query(getPostingQuery,
                    (rs, rowNum) -> new PostingMore(
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
        }

        for(PostingMore p : postingList) {
            String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
            String getParams = String.valueOf(p.getPostingIdx());
            List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                    (rs, rowNum) -> new CommunityTag(
                            rs.getInt("ctIdx"),
                            rs.getString("name")
                    ), getParams);

            p.setTags(tags);
        }

        return new GetRecommRes(user, 1, communityTags, recommTag, postingList);
    }

    public GetPostingOpenRes getPostingOpen(int postingIdx) {
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
                "where p.postingIdx = ? ";
        String getPostingParams = String.valueOf(postingIdx);
        PostingMore posting = this.jdbcTemplate.queryForObject(getPostingQuery,
                (rs, rowNum) -> new PostingMore(
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

        String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
        String getParams = String.valueOf(posting.getPostingIdx());
        List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new CommunityTag(
                        rs.getInt("ctIdx"),
                        rs.getString("name")
                ), getParams);

        posting.setTags(tags);

        String getCommentQuery = "select c.commentIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, " +
                "case when timestampdiff(hour, c.createdAt, now()) < 1 then concat(timestampdiff(minute, c.createdAt, now()), '분 전') " +
                "else case when datediff(now(), c.createdAt) >= 1 then date_format(c.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, c.createdAt, now()), '시간 전') end end as date, c.content " +
                "from Comment c JOIN Posting p ON c.postingIdx = p.postingIdx JOIN User u ON c.userIdx = u.userIdx " +
                "where c.postingIdx = ? " +
                "ORDER BY c.createdAt ASC";
        String getCommentParams = String.valueOf(postingIdx);
        List<Comment> commentList = this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new Comment(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("content")
                ), getCommentParams);


        return new GetPostingOpenRes(posting, commentList);
    }

    public GetPostingRes getPosting(int postingIdx, int userIdx) {
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
                "where p.postingIdx = ? ";
        Object[] getPostingParams = new Object[]{userIdx, userIdx, postingIdx};
        PostingMore posting = this.jdbcTemplate.queryForObject(getPostingQuery,
                (rs, rowNum) -> new PostingMore(
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

        String getQuery = "select ct.ctIdx, ct.name from Posting p JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and pt.ctIdx != 2";
        String getParams = String.valueOf(posting.getPostingIdx());
        List<CommunityTag> tags = this.jdbcTemplate.query(getQuery,
                (rs, rowNum) -> new CommunityTag(
                        rs.getInt("ctIdx"),
                        rs.getString("name")
                ), getParams);

        posting.setTags(tags);

        String getCommentQuery = "select c.commentIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, " +
                "case when timestampdiff(hour, c.createdAt, now()) < 1 then concat(timestampdiff(minute, c.createdAt, now()), '분 전') " +
                "else case when datediff(now(), c.createdAt) >= 1 then date_format(c.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, c.createdAt, now()), '시간 전') end end as date, c.content " +
                "from Comment c JOIN Posting p ON c.postingIdx = p.postingIdx JOIN User u ON c.userIdx = u.userIdx " +
                "where c.postingIdx = ? " +
                "ORDER BY c.createdAt ASC";
        String getCommentParams = String.valueOf(postingIdx);
        List<Comment> commentList = this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new Comment(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("content")
                ), getCommentParams);

        String getUserQuery = "select u.userIdx, u.imageUrl, case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name from User u where u.userIdx = ?";
        String getUserParams = String.valueOf(userIdx);

        DetailUser detailUser = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new DetailUser(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("name")
                ), getUserParams);


        return new GetPostingRes(posting, commentList, detailUser);
    }
}
