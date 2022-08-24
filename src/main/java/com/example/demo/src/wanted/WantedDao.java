package com.example.demo.src.wanted;

import com.example.demo.src.wanted.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class WantedDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetWantedRes getWanted(int userIdx) {
        String getUserQuery = "select imageUrl, name, email, phone from User where userIdx=?";
        String getUserParams = String.valueOf(userIdx);
        User user = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new User(
                        rs.getString("imageUrl"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")),
                getUserParams);

        String getTagQuery = "select h.homecategoryIdx, h.homecategory from User u " +
                "JOIN UserInterestTag ut ON ut.userIdx = u.userIdx " +
                "JOIN HomeCategory h ON h.homecategoryIdx = ut.homecategoryIdx " +
                "where u.userIdx=? and ut.status = 'ACTIVE'";
        String getTagParams = String.valueOf(userIdx);
        List<InterestTag> interestTags = this.jdbcTemplate.query(getTagQuery,
                (rs, rowNum) -> new InterestTag(
                        rs.getInt("homecategoryIdx"),
                        rs.getString("homecategory")),
                getTagParams);

        String getPointQuery = "select p.isPlus, p.cost from User u " +
                "JOIN Point p ON p.userIdx = u.userIdx " +
                "where u.userIdx=? and date_format(p.endAt, '%Y%m%d') >= date_format(now(), '%Y%m%d')";
        String getPointParams = String.valueOf(userIdx);
        List<Point> points = this.jdbcTemplate.query(getPointQuery,
                (rs, rowNum) -> new Point(
                        rs.getInt("isPlus"),
                        rs.getInt("cost")),
                getPointParams);

        int point = 0;

        for(Point p : points) {
            if(p.getIsPlus() == 1) {
                point += p.getCost();
            } else {
                point -= p.getCost();
            }
        }

        if(point < 0) {
            point = 0;
        }

        String getApplicationQuery = "select a.state from User u " +
                "JOIN Application a ON a.userIdx = u.userIdx " +
                "where u.userIdx=? and state != 0";
        String getApplicationParams = String.valueOf(userIdx);
        List<Integer> applications = this.jdbcTemplate.query(getApplicationQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("state")),
                getApplicationParams);
        ApplicationStatus applicationStatus = new ApplicationStatus(0, 0, 0, 0);
        for(int a : applications) {
            if(a == 1) {
                int n = applicationStatus.getCompleted();
                n += 1;
                applicationStatus.setCompleted(n);
            } else if (a == 2) {
                int n = applicationStatus.getBrowse();
                n += 1;
                applicationStatus.setBrowse(n);
            } else if (a == 3) {
                int n = applicationStatus.getPass();
                n += 1;
                applicationStatus.setPass(n);
            } else if (a == 4) {
                int n = applicationStatus.getFail();
                n += 1;
                applicationStatus.setFail(n);
            }
        }

        String getResumeQuery = "select resumeIdx from Resume r where r.userIdx = ? LIMIT 1";
        String getResumeParams = String.valueOf(userIdx);
        String resumeIdx = this.jdbcTemplate.queryForObject(getResumeQuery,
                (rs, rowNum) -> new String(
                        String.valueOf(rs.getInt("resumeIdx"))),
                getResumeParams);

        String getUserInfoQuery;
        Object[] getUserInfoParams;
        if(resumeIdx != null) {
            getUserInfoQuery = "select c.company, ec.category, es.subcategory from User u " +
                    "JOIN Resume r ON r.userIdx = u.userIdx " +
                    "JOIN Career c ON c.resumeIdx = r.resumeIdx " +
                    "JOIN Specialty s ON s.userIdx = u.userIdx " +
                    "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                    "JOIN EmploymentSubCategory es ON es.subcategoryIdx = s.categoryIdx " +
                    "where u.userIdx=? and r.resumeIdx=?";
            getUserInfoParams = new Object[]{userIdx, resumeIdx};
        } else {
            getUserInfoQuery = "select ec.category, es.subcategory from User u " +
                    "JOIN Specialty s ON s.userIdx = u.userIdx " +
                    "JOIN EmploymentCategory ec ON ec.categoryIdx = s.categoryIdx " +
                    "JOIN EmploymentSubCategory es ON es.subcategoryIdx = s.categoryIdx " +
                    "where u.userIdx=?";
            getUserInfoParams = new Object[]{userIdx};
        }

        UserInfo userInfo = this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (rs, rowNum) -> new UserInfo(
                        rs.getString("company"),
                        rs.getString("category"),
                        rs.getString("subcategory")),
                getUserInfoParams);

        String info = userInfo.getCompanyName() + ", " + userInfo.getJob() + ", " + userInfo.getDuty();

        int percent = 0;
        String getCheckQuery = "select exists(select resumeIdx from Resume where userIdx= ?) as isResume, " +
                "exists(select specialtyIdx from Specialty where userIdx= ?) as isSpecialty, " +
                "exists(select e.educationIdx from Education e JOIN Resume r ON e.resumeIdx = r.resumeIdx where r.userIdx= ?) as isEducation, " +
                "exists(select c.careerIdx from Career c JOIN Resume r ON c.resumeIdx = r.resumeIdx where r.userIdx= ?) as isCareer, " +
                "exists(select introduce from Resume where userIdx= ? and introduce != '') as isIntroduce, " +
                "introduce From Resume where userIdx = ? LIMIT 1";
        Object[] getCheckParams = new Object[]{userIdx, userIdx, userIdx, userIdx, userIdx, userIdx};
        ProfileCheck profileCheck = this.jdbcTemplate.queryForObject(getCheckQuery,
                (rs, rowNum) -> new ProfileCheck(
                        rs.getInt("isResume"),
                        rs.getInt("isSpecialty"),
                        rs.getInt("isEducation"),
                        rs.getInt("isCareer"),
                        rs.getInt("isIntroduce"),
                        rs.getString("introduce")),
                getCheckParams);

        if(profileCheck.getIsSpecialty() == 0) {
            percent = 30;
        } else {
            if(profileCheck.getIsResume() == 0) {
                percent = 40;
            } else {
                if(profileCheck.getIsEducation() == 1 && profileCheck.getIsCareer() == 1 && profileCheck.getIsIntroduce() == 1) {
                    if(profileCheck.getIntroduce().length() >= 400) {
                        percent = 100;
                    } else {
                        percent = 70;
                    }
                } else if(profileCheck.getIsEducation() == 1 || profileCheck.getIsCareer() == 1 || profileCheck.getIsIntroduce() == 1) {
                    percent = 55;
                }
            }
        }

        Profile profile = new Profile(info, percent);



        String getBookmarkQuery = "select e.employmentIdx, c.logoUrl, e.employment, c.companyName, r.name as region, n.name as nation " +
                "from User u JOIN Bookmark b ON b.userIdx = u.userIdx " +
                "JOIN Employment e ON e.employmentIdx = b.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.companyIdx " +
                "JOIN EmpRegion er ON e.employmentIdx = er.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx " +
                "JOIN Region r ON r.regionIdx = er.regionIdx " +
                "where u.userIdx=? and b.status = 'ACTIVE'";
        String getBookmarkParams = String.valueOf(userIdx);
        List<Employment> bookmarks = this.jdbcTemplate.query(getBookmarkQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation")),
                getBookmarkParams);

        String getLikeQuery = "select e.employmentIdx, c.logoUrl, e.employment, c.companyName, r.name as region, n.name as nation " +
                "from User u JOIN LikeEmp le ON le.userIdx = u.userIdx " +
                "JOIN Employment e ON e.employmentIdx = le.employmentIdx " +
                "JOIN Company c ON c.companyIdx = e.companyIdx " +
                "JOIN EmpRegion er ON e.employmentIdx = er.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx " +
                "JOIN Region r ON r.regionIdx = er.regionIdx " +
                "where u.userIdx=? and le.status = 'ACTIVE'";
        String getLikeParams = String.valueOf(userIdx);
        List<Employment> likeEmps = this.jdbcTemplate.query(getLikeQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation")),
                getLikeParams);



        return new GetWantedRes(user, interestTags, point, 0, 0, 0, applicationStatus, profile, bookmarks, likeEmps);
    }

    public GetInterestTagRes getInterestTag(int userIdx) {
        String getUserQuery = "select name from User where userIdx = ?";
        String getUserParams = String.valueOf(userIdx);
        String username = this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new String(
                        rs.getString("name")),
                getUserParams);

        String getBigQuery = "select itIdx, name from InterestTag";
        List<TagSet> tagSets = this.jdbcTemplate.query(getBigQuery,
                (rs, rowNum) -> new TagSet(
                        rs.getInt("itIdx"),
                        rs.getString("name")));

        for(TagSet t : tagSets) {
            String getTagQuery = "select h.homecategoryIdx, h.homecategory, " +
                    "case when(select ut.utIdx from UserInterestTag ut JOIN User u ON ut.userIdx = u.userIdx where ut.homecategoryIdx = h.homecategoryIdx and ut.userIdx = ? and ut.status = 'ACTIVE') " +
                    "is not null then 1 else 0 end as isInterest from HomeCategory h " +
                    "JOIN InterestClassification ic ON ic.homecategoryIdx = h.homecategoryIdx " +
                    "where ic.itIdx = ?";
            Object[] getTagParams = new Object[]{ userIdx, t.getItIdx()};

            List<InterestTagSet> tags = this.jdbcTemplate.query(getTagQuery,
                    (rs, rowNum) -> new InterestTagSet(
                            rs.getInt("homecategoryIdx"),
                            rs.getString("homecategory"),
                            rs.getInt("isInterest")),
                    getTagParams);

            t.setTags(tags);
        }


        return new GetInterestTagRes(username,tagSets);
    }

    public void setInterestTag(int userIdx, List<Integer> tags) {
        for(int t : tags) {
            String checkQuery = "select exists(select utIdx from UserInterestTag where userIdx = ? and homecategoryIdx = ?)";
            Object[] checkParams = new Object[]{userIdx, t};
            int isCheck = this.jdbcTemplate.queryForObject(checkQuery,
                    int.class,
                    checkParams);

            if(isCheck == 0) {
                String createQuery = "insert into UserInterestTag(homecategoryIdx, userIdx) VALUES(?,?)";
                Object[] createParams = new Object[]{t, userIdx};
                this.jdbcTemplate.update(createQuery,createParams);
            } else {
                String modifyQuery = "update UserInterestTag set status = 'ACTIVE' where userIdx=? and homecategoryIdx = ?";
                Object[] modifyParams = new Object[]{userIdx, t};
                this.jdbcTemplate.update(modifyQuery,modifyParams);
            }
        }

        String getRecordQuery = "select homecategoryIdx from UserInterestTag where userIdx = ?";
        String getRecordParams = String.valueOf(userIdx);
        List<Integer> records = this.jdbcTemplate.query(getRecordQuery,
                (rs, rowNum) -> new Integer(
                        rs.getInt("homecategoryIdx")),
                getRecordParams);

        records.removeAll(tags);

        for(int r : records) {
            String deleteQuery = "update UserInterestTag set status = 'INACTIVE' where userIdx = ? and homecategoryIdx = ?";
            Object[] deleteParams = new Object[]{userIdx, r};
            this.jdbcTemplate.update(deleteQuery, deleteParams);
        }
    }
}
