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

    public int checkPosting(int userIdx, int postingIdx) {
        String checkQuery = "select exists(select postingIdx from Posting where userIdx = ? and postingIdx = ?)";
        Object[] checkParams = new Object[]{userIdx, postingIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int checkComment(int userIdx, int postingIdx, int commentIdx) {
        String checkQuery = "select exists(select commentIdx from Comment where userIdx = ? and postingIdx = ? and commentIdx = ?)";
        Object[] checkParams = new Object[]{userIdx, postingIdx, commentIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int checkPostingActive(int userIdx, int postingIdx) {
        String checkQuery = "select exists(select postingIdx from Posting where userIdx = ? and postingIdx = ? and status='ACTIVE')";
        Object[] checkParams = new Object[]{userIdx, postingIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int checkPostingForComment(int postingIdx) {
        String checkQuery = "select exists(select postingIdx from Posting where postingIdx = ? and status='ACTIVE')";
        Object[] checkParams = new Object[]{postingIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public int checkLikes(int userIdx, int postingIdx) {
        String checkQuery = "select exists(select likePostIdx from LikePost where userIdx = ? and postingIdx = ?)";
        Object[] checkParams = new Object[]{userIdx, postingIdx};
        return this.jdbcTemplate.queryForObject(checkQuery,
                int.class,
                checkParams);
    }

    public void deletePosting(int userIdx, int postingIdx) {
        String deleteQuery = "update Posting set status = 'INACTIVE' where userIdx=? and postingIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, postingIdx};
        this.jdbcTemplate.update(deleteQuery, deleteParams);
        String resetTagQuery = "delete from PostingTag where postingIdx = ?";
        String resetTagParams = String.valueOf(postingIdx);
        this.jdbcTemplate.update(resetTagQuery, resetTagParams);
    }

    public void deleteComment(int userIdx, int postingIdx, int commentIdx) {
        String deleteQuery = "update Comment set status = 'INACTIVE' where userIdx=? and postingIdx = ? and commentIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, postingIdx, commentIdx};
        this.jdbcTemplate.update(deleteQuery, deleteParams);
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
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = ? and p.status = 'ACTIVE' " +
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
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?  and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?  and c.status='ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = ? and p.status = 'ACTIVE' " +
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
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = 2 and p.status = 'ACTIVE' " +
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
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?  and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?  and c.status='ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = 2 and p.status = 'ACTIVE' " +
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
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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

    public GetMyPostRes getMyPost(int userIdx) {
        String getUserQuery = "select u.userIdx, u.imageUrl, case when u.isNickname = 0 then u.name else u.nickname end as myName, " +
                "case when u.isNickname = 0 then '기본' else '닉네임' end as type, " +
                "ec.category as myJob, case when s.career = 0 then '신입' else concat(s.career, '년차') end as myCareer " +
                "from User u JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where u.userIdx = ?";
        String getUserParams = String.valueOf(userIdx);

        GetMyPostRes getMyPostRes = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetMyPostRes(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("myName"),
                        rs.getString("type"),
                        rs.getString("myJob"),
                        rs.getString("myCareer")
                ), getUserParams);

        String getPostingQuery = "select p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, " +
                "case when timestampdiff(hour, p.createdAt, now()) < 1 then concat(timestampdiff(minute, p.createdAt, now()), '분 전') " +
                "else case when datediff(now(), p.createdAt) >= 1 then date_format(p.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, p.createdAt, now()), '시간 전') end end as date, p.title, p.content, p.imageUrl, " +
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ? and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ? and c.status = 'ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx and c.status = 'ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "where p.status = 'ACTIVE' and p.userIdx = ? " +
                "ORDER BY p.createdAt DESC";
        Object[] getPostingParams = new Object[]{userIdx, userIdx, userIdx};
        List<MyPost> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new MyPost(
                        rs.getInt("postingIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imageUrl"),
                        rs.getInt("isLike"),
                        rs.getInt("likeNum"),
                        rs.getInt("isComment"),
                        rs.getInt("commentNum")
                ), getPostingParams);

        getMyPostRes.setPostList(postingList);

        return getMyPostRes;
    }

    public GetMyCommentRes getMyComment(int userIdx) {
        String getUserQuery = "select u.userIdx, u.imageUrl, case when u.isNickname = 0 then u.name else u.nickname end as myName, " +
                "case when u.isNickname = 0 then '기본' else '닉네임' end as type, " +
                "ec.category as myJob, case when s.career = 0 then '신입' else concat(s.career, '년차') end as myCareer " +
                "from User u JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where u.userIdx = ?";
        String getUserParams = String.valueOf(userIdx);

        GetMyCommentRes getMyCommentRes = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetMyCommentRes(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("myName"),
                        rs.getString("type"),
                        rs.getString("myJob"),
                        rs.getString("myCareer")
                ), getUserParams);


        String getCommentQuery = "select u.userIdx, c.commentIdx, c.content, case when timestampdiff(hour, c.createdAt, now()) < 1 then concat(timestampdiff(minute, c.createdAt, now()), '분 전') " +
                                "else case when datediff(now(), c.createdAt) >= 1 then date_format(c.createdAt, '%Y.%m.%d') " +
                                "else concat(timestampdiff(hour, c.createdAt, now()), '시간 전') end end as date, p.postingIdx, p.title, " +
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ? and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ? and c.status = 'ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx and c.status = 'ACTIVE') as commentNum " +
                "from Comment c JOIN User u ON c.userIdx = u.userIdx JOIN Posting p ON c.postingIdx = p.postingIdx " +
                "where p.status = 'ACTIVE' and c.status = 'ACTIVE' and c.userIdx = ? " +
                "ORDER BY p.createdAt DESC";
        Object[] getCommentParams = new Object[]{ userIdx, userIdx, userIdx};
        List<MyComment> commentList = this.jdbcTemplate.query(getCommentQuery,
                (rs, rowNum) -> new MyComment(
                        rs.getInt("userIdx"),
                        rs.getInt("commentIdx"),
                        rs.getString("content"),
                        rs.getString("date"),
                        rs.getInt("postingIdx"),
                        rs.getString("title"),
                        rs.getInt("isLike"),
                        rs.getInt("likeNum"),
                        rs.getInt("isComment"),
                        rs.getInt("commentNum")
                ), getCommentParams);

        getMyCommentRes.setCommentList(commentList);

        return getMyCommentRes;
    }

    public GetMyLikeRes getMyLike(int userIdx) {
        String getUserQuery = "select u.userIdx, u.imageUrl, case when u.isNickname = 0 then u.name else u.nickname end as myName, " +
                "case when u.isNickname = 0 then '기본' else '닉네임' end as type, " +
                "ec.category as myJob, case when s.career = 0 then '신입' else concat(s.career, '년차') end as myCareer " +
                "from User u JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where u.userIdx = ?";
        String getUserParams = String.valueOf(userIdx);

        GetMyLikeRes getMyLikeRes = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetMyLikeRes(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("myName"),
                        rs.getString("type"),
                        rs.getString("myJob"),
                        rs.getString("myCareer")
                ), getUserParams);

        String getPostingQuery = "select p.postingIdx, u.userIdx, u.imageUrl as profileUrl, " +
                "case when u.isNickname = 0 " +
                "then u.name else u.nickname end as name, " +
                "case when timestampdiff(hour, p.createdAt, now()) < 1 then concat(timestampdiff(minute, p.createdAt, now()), '분 전') " +
                "else case when datediff(now(), p.createdAt) >= 1 then date_format(p.createdAt, '%Y.%m.%d') " +
                "else concat(timestampdiff(hour, p.createdAt, now()), '시간 전') end end as date, p.title, p.content, p.imageUrl, " +
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ? and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ? and c.status = 'ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx and c.status = 'ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx JOIN LikePost lp ON lp.postingIdx = p.postingIdx " +
                "where p.status = 'ACTIVE' and lp.userIdx = ? and lp.status = 'ACTIVE' " +
                "ORDER BY p.createdAt DESC";
        Object[] getPostingParams = new Object[]{userIdx, userIdx, userIdx};
        List<MyPost> postingList = this.jdbcTemplate.query(getPostingQuery,
                (rs, rowNum) -> new MyPost(
                        rs.getInt("postingIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("profileUrl"),
                        rs.getString("name"),
                        rs.getString("date"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("imageUrl"),
                        rs.getInt("isLike"),
                        rs.getInt("likeNum"),
                        rs.getInt("isComment"),
                        rs.getInt("commentNum")
                ), getPostingParams);

        getMyLikeRes.setPostList(postingList);

        return getMyLikeRes;
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
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                "where pt.ctIdx = 2 and p.status = 'ACTIVE' " +
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
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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
                    "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?  and lp.status='ACTIVE') as isLike, " +
                    "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                    "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?  and c.status='ACTIVE') as isComment, " +
                    "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                    "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                    "JOIN Specialty s ON u.userIdx = s.userIdx " +
                    "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                    "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "where pt.ctIdx = 2 and p.status = 'ACTIVE' " +
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
                    "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?  and lp.status='ACTIVE') as isLike, " +
                    "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                    "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?  and c.status='ACTIVE') as isComment, " +
                    "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                    "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                    "JOIN Specialty s ON u.userIdx = s.userIdx " +
                    "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                    "JOIN PostingTag pt ON pt.postingIdx = p.postingIdx " +
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx " +
                    "where p.status = 'ACTIVE' and ct.name IN (select h.homecategory from User u JOIN UserInterestTag ut ON u.userIdx = ut.userIdx JOIN HomeCategory h ON h.homecategoryIdx = ut.homecategoryIdx) " +
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
                    "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where p.postingIdx = ? and p.status = 'ACTIVE'";
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
                "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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
                "where c.postingIdx = ? and c.status = 'ACTIVE'" +
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
                "exists(select likePostIdx from LikePost lp where lp.postingIdx = p.postingIdx and lp.userIdx = ?  and lp.status='ACTIVE') as isLike, " +
                "(select count(likePostIdx) from LikePost lp where lp.postingIdx = p.postingIdx  and lp.status='ACTIVE') as likeNum, " +
                "exists(select commentIdx from Comment c where c.postingIdx = p.postingIdx and c.userIdx = ?  and c.status='ACTIVE') as isComment, " +
                "(select count(commentIdx) from Comment c where c.postingIdx = p.postingIdx  and c.status='ACTIVE') as commentNum " +
                "from Posting p JOIN User u ON p.userIdx = u.userIdx " +
                "JOIN Specialty s ON u.userIdx = s.userIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                "where p.postingIdx = ? and p.status = 'ACTIVE'";
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
                "JOIN CommunityTag ct ON ct.ctIdx = pt.ctIdx where p.postingIdx = ? and p.status = 'ACTIVE' and pt.ctIdx != 2";
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
                "where c.postingIdx = ? and c.status = 'ACTIVE'" +
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

    public GetProfileRes getProfile(int userIdx) {
        String getProfileQuery = "select u.userIdx, u.imageUrl, u.name, u.isNickname, case when u.nickname is null then '' else u.nickname end as nickname from User u where u.userIdx = ?";
        String getProfileParams = String.valueOf(userIdx);

        return this.jdbcTemplate.queryForObject(getProfileQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("userIdx"),
                        rs.getString("imageUrl"),
                        rs.getString("name"),
                        rs.getInt("u.isNickname"),
                        rs.getString("nickname")
                ), getProfileParams);

    }

    public void setProfile(int userIdx, PatchProfileReq patchProfileReq) {
        String setProfileQuery = "update User set isNickname = ?, nickname = ? where userIdx = ?";
        Object[] setProfileParams = new Object[]{patchProfileReq.getIsNickname(), patchProfileReq.getNickname(), userIdx};

        this.jdbcTemplate.update(setProfileQuery, setProfileParams);
    }

    public PostPostingRes createPosting(int userIdx, PostPostingReq postPostingReq) {
        String createPostQuery = "insert into Posting (title, content, userIdx) VALUES(?,?,?)";
        Object[] createPostParams = new Object[]{postPostingReq.getTitle(), postPostingReq.getContent(), userIdx};
        this.jdbcTemplate.update(createPostQuery, createPostParams);
        String lastIdQuery = "select last_insert_id()";
        int postingIdx = this.jdbcTemplate.queryForObject(lastIdQuery, int.class);

        for(int tagIdx : postPostingReq.getTags()) {
            String getTagQuery = "select ct.ctIdx from CommunityTag ct where ct.name = (select h.homecategory from HomeCategory h where h.homecategoryIdx = ?)";
            String getTagParams = String.valueOf(tagIdx);
            int t = this.jdbcTemplate.queryForObject(getTagQuery, int.class, getTagParams);

            String createTagQuery = "insert into PostingTag (postingIdx, ctIdx) VALUES(?,?)";
            Object[] createTagParams = new Object[]{postingIdx, t};
            this.jdbcTemplate.update(createTagQuery, createTagParams);
        }

        return new PostPostingRes(postingIdx);
    }

    public PostCommentRes createComment(int postingIdx, int userIdx, PostCommentReq postCommentReq) {
        String createPostQuery = "insert into Comment (content, userIdx, postingIdx) VALUES(?,?,?)";
        Object[] createPostParams = new Object[]{postCommentReq.getContent(), userIdx, postingIdx};
        this.jdbcTemplate.update(createPostQuery, createPostParams);
        String lastIdQuery = "select last_insert_id()";
        int commentIdx = this.jdbcTemplate.queryForObject(lastIdQuery, int.class);
        return new PostCommentRes(postingIdx, commentIdx);
    }

    public PutPostingRes updatePosting(int userIdx, PutPostingReq putPostingReq) {
        String updatePostQuery = "update Posting set title = ?, content = ? where postingIdx = ? and userIdx = ?";
        Object[] updatePostParams = new Object[]{putPostingReq.getTitle(), putPostingReq.getContent(), putPostingReq.getPostingIdx(), userIdx};
        this.jdbcTemplate.update(updatePostQuery, updatePostParams);

        String checkPostQuery = "select postingIdx from Posting where postingIdx = ? and userIdx = ?";
        Object[] checkPostParams = new Object[]{putPostingReq.getPostingIdx(), userIdx};
        int postingIdx = this.jdbcTemplate.queryForObject(checkPostQuery, int.class, checkPostParams);

        String resetTagQuery = "delete from PostingTag where postingIdx = ?";
        String resetTagParams = String.valueOf(postingIdx);
        this.jdbcTemplate.update(resetTagQuery, resetTagParams);

        for(int tagIdx : putPostingReq.getTags()) {
            String getTagQuery = "select ct.ctIdx from CommunityTag ct where ct.name = (select h.homecategory from HomeCategory h where h.homecategoryIdx = ?)";
            String getTagParams = String.valueOf(tagIdx);
            int t = this.jdbcTemplate.queryForObject(getTagQuery, int.class, getTagParams);

            String createTagQuery = "insert into PostingTag (postingIdx, ctIdx) VALUES(?,?)";
            Object[] createTagParams = new Object[]{postingIdx, t};
            this.jdbcTemplate.update(createTagQuery, createTagParams);
        }

        return new PutPostingRes(postingIdx);
    }

    public PostPostingRes createPostingWithImage(int userIdx, String imageUrl, PostPostingReq postPostingReq) {
        String createPostQuery = "insert into Posting (title, content, imageUrl, userIdx) VALUES(?,?,?,?)";
        Object[] createPostParams = new Object[]{postPostingReq.getTitle(), postPostingReq.getContent(), imageUrl, userIdx};
        this.jdbcTemplate.update(createPostQuery, createPostParams);
        String lastIdQuery = "select last_insert_id()";
        int postingIdx = this.jdbcTemplate.queryForObject(lastIdQuery, int.class);

        for(int tagIdx : postPostingReq.getTags()) {
            String getTagQuery = "select ct.ctIdx from CommunityTag ct where ct.name = (select h.homecategory from HomeCategory h where h.homecategoryIdx = ?)";
            String getTagParams = String.valueOf(tagIdx);
            int t = this.jdbcTemplate.queryForObject(getTagQuery, int.class, getTagParams);

            String createTagQuery = "insert into PostingTag (postingIdx, ctIdx) VALUES(?,?)";
            Object[] createTagParams = new Object[]{postingIdx, t};
            this.jdbcTemplate.update(createTagQuery, createTagParams);
        }

        return new PostPostingRes(postingIdx);
    }

    public PutPostingRes updatePostingWithImage(int userIdx, String imageUrl, PutPostingReq putPostingReq) {
        String updatePostQuery = "update Posting set title = ?, content = ?, imageUrl = ? where postingIdx = ? and userIdx = ?";
        Object[] updatePostParams = new Object[]{putPostingReq.getTitle(), putPostingReq.getContent(), imageUrl, putPostingReq.getPostingIdx(), userIdx};
        this.jdbcTemplate.update(updatePostQuery, updatePostParams);

        String checkPostQuery = "select postingIdx from Posting where postingIdx = ? and userIdx = ?";
        Object[] checkPostParams = new Object[]{putPostingReq.getPostingIdx(), userIdx};
        int postingIdx = this.jdbcTemplate.queryForObject(checkPostQuery, int.class, checkPostParams);

        String resetTagQuery = "delete from PostingTag where postingIdx = ?";
        String resetTagParams = String.valueOf(postingIdx);
        this.jdbcTemplate.update(resetTagQuery, resetTagParams);

        for(int tagIdx : putPostingReq.getTags()) {
            String getTagQuery = "select ct.ctIdx from CommunityTag ct where ct.name = (select h.homecategory from HomeCategory h where h.homecategoryIdx = ?)";
            String getTagParams = String.valueOf(tagIdx);
            int t = this.jdbcTemplate.queryForObject(getTagQuery, int.class, getTagParams);

            String createTagQuery = "insert into PostingTag (postingIdx, ctIdx) VALUES(?,?)";
            Object[] createTagParams = new Object[]{postingIdx, t};
            this.jdbcTemplate.update(createTagQuery, createTagParams);
        }

        return new PutPostingRes(postingIdx);
    }

    public int modifyLikes(int userIdx, int postingIdx) {
        String modifyQuery = "update LikePost set status = 'ACTIVE' where userIdx=? and postingIdx = ?";
        Object[] modifyParams = new Object[]{userIdx, postingIdx};
        this.jdbcTemplate.update(modifyQuery,modifyParams);

        String getQuery = "select likePostIdx from LikePost where userIdx=? and postingIdx = ?";
        Object[] getParams = new Object[]{userIdx, postingIdx};
        return this.jdbcTemplate.queryForObject(getQuery,
                int.class,
                getParams);
    }

    public int createLikes(int userIdx, int postingIdx) {
        String createQuery = "insert into LikePost (userIdx, postingIdx) VALUES (?,?)";
        Object[] createParams = new Object[]{userIdx, postingIdx};
        this.jdbcTemplate.update(createQuery,createParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public void deleteLikes(int userIdx, int postingIdx) {
        String deleteQuery = "update LikePost set status = 'INACTIVE' where userIdx=? and postingIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, postingIdx};
        this.jdbcTemplate.update(deleteQuery, deleteParams);
    }

}
