package com.example.demo.src.position;

import com.example.demo.src.like.model.GetRewardRes;
import com.example.demo.src.position.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PositionDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetOpenPositionRes getPositionOpen(int jobIdx, int dutyIdx) {
        String getJobQuery = "select categoryIdx, category from EmploymentCategory where categoryIdx=?";
        String getJobParams = String.valueOf(jobIdx);

        JobCategory jobCategory = this.jdbcTemplate.queryForObject(getJobQuery,
                (rs, rowNum) -> new JobCategory(
                        rs.getInt("categoryIdx"),
                        rs.getString("category")),
                getJobParams);

        String getDutyQuery = "select subcategoryIdx, subcategory from EmploymentSubCategory where subcategoryIdx=?";
        String getDutyParams = String.valueOf(dutyIdx);

        DutyCategory dutyCategory = this.jdbcTemplate.queryForObject(getDutyQuery,
                (rs, rowNum) -> new DutyCategory(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        EmpRegion empRegion = new EmpRegion(5, "한국", 5, "전국");

        String getSearchCategory = "select searchcategoryIdx, searchcategory from SearchCategory";
        List<SearchCategory> searchCategories = this.jdbcTemplate.query(getSearchCategory,
                (rs, rowNum) -> new SearchCategory(
                        rs.getInt("searchcategoryIdx"),
                        rs.getString("searchcategory")));

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c ORDER BY RAND() LIMIT 5";
        List<Company> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new Company(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")));

        for (Company c : companies) {
            String getThumbnailQuery = "select companyImg from CompanyImg where companyIdx=?";
            String getThumbnailParams = String.valueOf(c.getCompanyIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("companyImg")),
                    getThumbnailParams);

            if(thumbnails.size() != 0) {
                c.setThumbnail(thumbnails.get(0));
            }


            String getPositionQuery = "select COUNT(*) as position from Employment where companyIdx=?;";
            String getPositionParams = String.valueOf(c.getCompanyIdx());

            int position = this.jdbcTemplate.queryForObject(getPositionQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("position")),
                    getPositionParams);

            c.setPosition(position);
        }


        String getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation from Employment e" +
                " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                "JOIN CategoryList cl ON cl.employmentIdx = e.employmentIdx " +
                "JOIN EmploymentSubCategory es ON es.subcategoryIdx = cl.subcategoryIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = es.categoryIdx " +
                "WHERE cl.subcategoryIdx = ? and ec.categoryIdx = ?";
        Object[] getEmploymentParams = new Object[]{dutyIdx, jobIdx};

        List<Employment> employments = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation")),
                        getEmploymentParams);

        for(Employment e : employments) {
            String getThumbnailQuery = "select employmentImg from EmploymentImg where employmentIdx=?";
            String getThumbnailParams = String.valueOf(e.getEmploymentIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("employmentImg")),
                    getThumbnailParams);

            e.setThumbnail(thumbnails.get(0));

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(e.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            e.setReward(reward);

            e.setIsBookmark(0);
        }

        return new GetOpenPositionRes(jobCategory, dutyCategory, empRegion, "전체", searchCategories, companies, employments);
    }



    public GetOpenPositionRes getPosition(int userIdx, int jobIdx, int dutyIdx) {
        String getJobQuery = "select categoryIdx, category from EmploymentCategory where categoryIdx=?";
        String getJobParams = String.valueOf(jobIdx);

        JobCategory jobCategory = this.jdbcTemplate.queryForObject(getJobQuery,
                (rs, rowNum) -> new JobCategory(
                        rs.getInt("categoryIdx"),
                        rs.getString("category")),
                getJobParams);

        String getDutyQuery = "select subcategoryIdx, subcategory from EmploymentSubCategory where subcategoryIdx=?";
        String getDutyParams = String.valueOf(dutyIdx);

        DutyCategory dutyCategory = this.jdbcTemplate.queryForObject(getDutyQuery,
                (rs, rowNum) -> new DutyCategory(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        EmpRegion empRegion = new EmpRegion(5, "한국", 5, "전국");

        String getSearchCategory = "select searchcategoryIdx, searchcategory from SearchCategory";
        List<SearchCategory> searchCategories = this.jdbcTemplate.query(getSearchCategory,
                (rs, rowNum) -> new SearchCategory(
                        rs.getInt("searchcategoryIdx"),
                        rs.getString("searchcategory")));

        String getCompanyQuery = "select c.companyIdx, c.logoUrl, c.companyName from Company c ORDER BY RAND() LIMIT 5";
        List<Company> companies = this.jdbcTemplate.query(getCompanyQuery,
                (rs, rowNum) -> new Company(
                        rs.getInt("companyIdx"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")));

        for (Company c : companies) {
            String getThumbnailQuery = "select companyImg from CompanyImg where companyIdx=?";
            String getThumbnailParams = String.valueOf(c.getCompanyIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("companyImg")),
                    getThumbnailParams);

            if(thumbnails.size() != 0) {
                c.setThumbnail(thumbnails.get(0));
            }


            String getPositionQuery = "select COUNT(*) as position from Employment where companyIdx=?;";
            String getPositionParams = String.valueOf(c.getCompanyIdx());

            int position = this.jdbcTemplate.queryForObject(getPositionQuery,
                    (rs, rowNum) -> new Integer(
                            rs.getInt("position")),
                    getPositionParams);

            c.setPosition(position);
        }


        String getEmploymentQuery = "select e.employmentIdx, e.employment, c.companyName, r.name as region, n.name as nation, " +
                "case when(select b.bookmarkIdx from Bookmark b where b.employmentIdx = e.employmentIdx and b.userIdx = ?) is not null then 1 else 0 end as isBookmark " +
                "from Employment e" +
                " JOIN Company c ON e.companyIdx = c.companyIdx JOIN EmpRegion er ON er.employmentIdx = e.employmentIdx " +
                "JOIN Nation n ON n.nationIdx = er.nationIdx JOIN Region r ON r.regionIdx = er.regionIdx " +
                "JOIN CategoryList cl ON cl.employmentIdx = e.employmentIdx " +
                "JOIN EmploymentSubCategory es ON es.subcategoryIdx = cl.subcategoryIdx " +
                "JOIN EmploymentCategory ec ON ec.categoryIdx = es.categoryIdx " +
                "WHERE cl.subcategoryIdx = ? and ec.categoryIdx = ?";
        Object[] getEmploymentParams = new Object[]{userIdx, dutyIdx, jobIdx};

        List<Employment> employments = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new Employment(
                        rs.getInt("employmentIdx"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("nation"),
                        rs.getInt("isBookmark")),
                getEmploymentParams);

        for(Employment e : employments) {
            String getThumbnailQuery = "select employmentImg from EmploymentImg where employmentIdx=?";
            String getThumbnailParams = String.valueOf(e.getEmploymentIdx());

            List<String> thumbnails = this.jdbcTemplate.query(getThumbnailQuery,
                    (rs, rowNum) -> new String (
                            rs.getString("employmentImg")),
                    getThumbnailParams);

            e.setThumbnail(thumbnails.get(0));

            String getRewardQuery = "select e.applicant, e.recommender from Employment e where e.employmentIdx =?";
            String getRewardParams = String.valueOf(e.getEmploymentIdx());
            GetRewardRes getRewardRes = this.jdbcTemplate.queryForObject(getRewardQuery,
                    (rs, rowNum) -> new GetRewardRes(
                            rs.getString("applicant"),
                            rs.getString("recommender")),
                    getRewardParams);
            int reward = Integer.parseInt(getRewardRes.getApplicant()) + Integer.parseInt(getRewardRes.getRecommender());
            e.setReward(reward);
        }

        return new GetOpenPositionRes(jobCategory, dutyCategory, empRegion, "전체", searchCategories, companies, employments);
    }

    public List<JobCategory> getJobCategory() {
        String getJobCategory = "select categoryIdx, category from EmploymentCategory";
        List<JobCategory> jobCategories = this.jdbcTemplate.query(getJobCategory,
                (rs, rowNum) -> new JobCategory(
                        rs.getInt("categoryIdx"),
                        rs.getString("category")));

        return jobCategories;
    }

    public List<DutyCategory> getDutyCategory(int jobIdx) {
        String getDutyCategory = "select subcategoryIdx, subcategory from EmploymentSubCategory where categoryIdx=?";
        String getDutyParams = String.valueOf(jobIdx);
        List<DutyCategory> dutyCategories = this.jdbcTemplate.query(getDutyCategory,
                (rs, rowNum) -> new DutyCategory(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                getDutyParams);

        return dutyCategories;
    }

    public List<Nation> getNations() {
        String getNationQuery = "select nationIdx, name from Nation";
        List<Nation> nations = this.jdbcTemplate.query(getNationQuery,
                (rs, rowNum) -> new Nation(
                        rs.getInt("nationIdx"),
                        rs.getString("name")));

        return nations;
    }

    public List<Region> getRegions(int nationIdx) {
        String getRegionQuery = "select regionIdx, name from Region where nationIdx=?";
        String getRegionParams = String.valueOf(nationIdx);
        List<Region> regions = this.jdbcTemplate.query(getRegionQuery,
                (rs, rowNum) -> new Region(
                        rs.getInt("regionIdx"),
                        rs.getString("name")),
                getRegionParams);

        return regions;
    }

    public List<DetailRegion> getDetailRegions(int regionIdx) {
        String getDetailRegionQuery = "select drIdx, name from DetailRegion where regionIdx=?";
        String getDetailRegionParams = String.valueOf(regionIdx);
        List<DetailRegion> detailRegions = this.jdbcTemplate.query(getDetailRegionQuery,
                (rs, rowNum) -> new DetailRegion(
                        rs.getInt("drIdx"),
                        rs.getString("name")),
                getDetailRegionParams);

        return detailRegions;
    }

    public List<EmpStack> getStacks() {
        String getStackQuery = "select skillIdx, name from Skill";
        List<EmpStack> stacks = this.jdbcTemplate.query(getStackQuery,
                (rs, rowNum) -> new EmpStack(
                        rs.getInt("skillIdx"),
                        rs.getString("name")));

        return stacks;
    }

}
