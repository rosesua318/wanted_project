package com.example.demo.src.employment;

import com.example.demo.src.company.model.GetCompanyInfoRes;
import com.example.demo.src.employment.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;

@Repository
public class EmploymentDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // 채용 포지션 조회(상세화면 x)(북마크조회관련)
    public List<GetEmploymentInfoRes> getEmploymentInfo (int userIdx) {

        String getEmploymentQuery =
                "SELECT EMP.employmentIdx,employmentImg,employment,companyName,N.name AS nation,CONCAT((applicant+recommender),'원') AS compensation FROM Employment AS EMP " +
                        "JOIN EmploymentImg AS EMPIMG ON EMPIMG.employmentIdx = EMP.employmentIdx " +
                        "JOIN Company AS C ON C.companyIdx = EMP.companyIdx " +
                        "JOIN Nation AS N " +
                        "JOIN EmpRegion AS EMPR ON EMPR.employmentIdx = EMP.employmentIdx AND N.nationIdx = EMPR.nationIdx " +
                        "JOIN Bookmark AS BM ON BM.employmentIdx = EMP.employmentIdx AND BM.status = 'ACTIVE' WHERE BM.userIdx= ?";

        int getEmploymentParams = userIdx;

        List<GetEmploymentInfoRes> getEmploymentInfoRes = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new GetEmploymentInfoRes(
                        rs.getInt("employmentIdx"),
                        rs.getString("employmentImg"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("nation"),
                        rs.getString("compensation")),
                getEmploymentParams);

        int count = 0;        // 이미지 1개만 나올 수 있도록.
        Iterator<GetEmploymentInfoRes> it = getEmploymentInfoRes.iterator();
        while (it.hasNext()) {
            int employmentIdx = it.next().getEmploymentIdx();
            if (employmentIdx == count) {

                it.remove();
            } else
                count = employmentIdx;
        }

        return getEmploymentInfoRes;

    }

//    채용 홈 화면 조회 (비회원 용)


    public GetEmpHomeRes getEmpHome() {

        String getBannerQuery = "select bannerIdx,imageUrl from Banner where type = 0";
        List<EmpBanner> empBanners = this.jdbcTemplate.query(getBannerQuery,
                (rs, rowNum) -> new EmpBanner(
                        rs.getInt("bannerIdx"),
                        rs.getString("imageUrl")));




        String getEmploymentQuery = "SELECT Employment.employmentIdx,EmploymentImg.employmentImg, Employment.employment, Company.companyName, CONCAT(R.name,'.',N.name) AS Region,CONCAT((applicant+recommender),'원') AS compensation FROM Employment "+
        " JOIN EmploymentImg ON EmploymentImg.employmentIdx = Employment.employmentIdx"+
        " JOIN Company ON Company.companyIdx = Employment.companyIdx"+
        " JOIN Nation AS N"+
        " JOIN Region AS R ON R.nationIdx = N.nationIdx"+
        " JOIN EmpRegion AS ER ON ER.nationIdx = N.nationIdx AND ER.employmentIdx = Employment.employmentIdx AND ER.regionIdx = R.regionIdx LIMIT 4";

        List<GetEmploymentInfoRes> employmentInfo = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new GetEmploymentInfoRes(

                        rs.getInt("employmentIdx"),
                        rs.getString("employmentImg"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("compensation")));

        int  empCnt = 0;
        // 이미지 1개만 나올 수 있도록.
        Iterator<GetEmploymentInfoRes> it2 = employmentInfo.iterator();
        while(it2.hasNext()){
            int employmentIdx = it2.next().getEmploymentIdx();
            if(employmentIdx == empCnt){
                it2.remove();
            }
            else
                empCnt = employmentIdx;
        }


        String getCompanyQuery = "SELECT  C.companyIdx ,CI.companyImg,C.logoUrl,C.companyName FROM Company AS C\n" +
                "JOIN CompanyImg AS CI ON CI.companyIdx = C.companyIdx  LIMIT 8 OFFSET 10";

        List<GetCompanyInfoRes> companyInfo = this.jdbcTemplate.query(getCompanyQuery,
                (rs,rowNum)-> new GetCompanyInfoRes(
                        rs.getInt("companyIdx"),
                        rs.getString("companyImg"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")
                ));

        int  companyCnt = 0;
        // 이미지 1개만 나올 수 있도록.
        Iterator<GetCompanyInfoRes> it3 = companyInfo.iterator();
        while(it3.hasNext()){
            int companyIdx = it3.next().getCompanyIdx();
            if(companyIdx == companyCnt){

                it3.remove();
            }
            else
                companyCnt = companyIdx;
        }

        return new GetEmpHomeRes("non-members", empBanners,employmentInfo,companyInfo);
    }

    // 회원 용 채용 홈페이지
    public GetEmpHomeRes getEmpHome(int userIdx) {



        String recommendQuery = "SELECT CONCAT(User.name,'님! 지금 바로 지원해볼까요?') AS \"recommend\" from User WHERE userIdx = ?;";

        int getRecommendParmas = userIdx;

        String recommend = this.jdbcTemplate.queryForObject(recommendQuery,
                (rs,rowNum) -> new String(
                        rs.getString("recommend")),
                getRecommendParmas);


        String getBannerQuery = "select bannerIdx,imageUrl from Banner where type = 0";
        List<EmpBanner> empBanners = this.jdbcTemplate.query(getBannerQuery,
                (rs, rowNum) -> new EmpBanner(
                        rs.getInt("bannerIdx"),
                        rs.getString("imageUrl")));


        String getEmploymentQuery = "SELECT Employment.employmentIdx,EmploymentImg.employmentImg, Employment.employment, Company.companyName, CONCAT(R.name,'.',N.name) AS Region,CONCAT((applicant+recommender),'원') AS compensation FROM Employment "+
                " JOIN EmploymentImg ON EmploymentImg.employmentIdx = Employment.employmentIdx"+
                " JOIN Company ON Company.companyIdx = Employment.companyIdx"+
                " JOIN Nation AS N"+
                " JOIN Region AS R ON R.nationIdx = N.nationIdx"+
                " JOIN EmpRegion AS ER ON ER.nationIdx = N.nationIdx AND ER.employmentIdx = Employment.employmentIdx AND ER.regionIdx = R.regionIdx LIMIT 4";

        List<GetEmploymentInfoRes> employmentInfo = this.jdbcTemplate.query(getEmploymentQuery,
                (rs, rowNum) -> new GetEmploymentInfoRes(

                        rs.getInt("employmentIdx"),
                        rs.getString("employmentImg"),
                        rs.getString("employment"),
                        rs.getString("companyName"),
                        rs.getString("region"),
                        rs.getString("compensation")));

        int  empCnt = 0;
        // 이미지 1개만 나올 수 있도록.
        Iterator<GetEmploymentInfoRes> it2 = employmentInfo.iterator();
        while(it2.hasNext()){
            int employmentIdx = it2.next().getEmploymentIdx();
            if(employmentIdx == empCnt){
                it2.remove();
            }
            else
                empCnt = employmentIdx;
        }


        String getCompanyQuery = "SELECT  C.companyIdx ,CI.companyImg,C.logoUrl,C.companyName FROM Company AS C\n" +
                "JOIN CompanyImg AS CI ON CI.companyIdx = C.companyIdx LIMIT 8;";

        List<GetCompanyInfoRes> companyInfo = this.jdbcTemplate.query(getCompanyQuery,
                (rs,rowNum)-> new GetCompanyInfoRes(
                        rs.getInt("companyIdx"),
                        rs.getString("companyImg"),
                        rs.getString("logoUrl"),
                        rs.getString("companyName")
                ));

        int  companyCnt = 0;
        // 이미지 1개만 나올 수 있도록.
        Iterator<GetCompanyInfoRes> it3 = companyInfo.iterator();
        while(it3.hasNext()){
            int companyIdx = it3.next().getCompanyIdx();
            if(companyIdx == companyCnt){

                it3.remove();
            }
            else
                companyCnt = companyIdx;
        }
        return new GetEmpHomeRes(recommend, empBanners,employmentInfo,companyInfo);
    }


//    public getEmpDetail(int userIdx, int employmentIdx){
//
//        String employmentImgQuery = "\n" +
//                " SELECT Employment.employmentIdx,EmploymentImg.employmentImg FROM EmploymentImg\n" +
//                "J OIN Employment ON EmploymentImg.employmentIdx = Employment.employmentIdx WHERE Employment.EmploymentIdx = ?;";
//
//        int getEmploymentImgParams = employmentIdx;
//
//
//
//    }
}