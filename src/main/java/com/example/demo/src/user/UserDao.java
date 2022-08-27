package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.xml.xpath.XPath;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.UPDATE_FAIL_SPECIALTY_SKILL;
import static com.example.demo.config.BaseResponseStatus.UPDATE_FAIL_TEST;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from User where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(

                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),

                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select userIdx, name, imageUrl, email, phone from User where userIdx = ?";
        int getUserParams = userIdx;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("imageUrl"),
                        rs.getString("email"),
                        rs.getString("phone")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (email, password, name,phone,imageUrl) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getEmail(), postUserReq.getPassword(), postUserReq.getName(), postUserReq.getPhone(),"https://static.wanted.co.kr/images/profile_default.png"};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ? AND status = 'ACTIVE')";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserInfo(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set name = ?, email = ?, phone = ? where userIdx = ? "; // 쿼리 문 확인 필요
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getName(), patchUserReq.getEmail(),patchUserReq.getPhone(),patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

//    public User getPwd(PostLoginReq postLoginReq){
//        String getPwdQuery = "select userIdx,email,password,name,phone from User where email = ?";
//        String getPwdParams = postLoginReq.getEmail();
//
//        return this.jdbcTemplate.queryForObject(getPwdQuery,
//                (rs,rowNum)-> new User(
//                        rs.getInt("userIdx"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("name"),
//                        rs.getString("phone")
//                ),
//                getPwdParams
//                );
//
//    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx,email,password,name,phone from User where email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("phone")
                ),
                getPwdParams
        );

    }

//상태변경
    public int modifyUserStatus(PatchUserStatusReq patchUserStatusReq){

        String modifyStatusQuery = "UPDATE User set status = ? where userIdx = ?";
        Object[] modifyStatusParams = new Object[]{patchUserStatusReq.getStatus(), patchUserStatusReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyStatusQuery,modifyStatusParams);
    }


    // 비밀번호 변경

    public int modifyPwd(PatchPwdReq patchPwdReq){

        String modfiyPwdQuery = "UPDATE User set password = ? where userIdx = ?";
        Object[] modifyPwdParams = new Object[]{patchPwdReq.getPassword(), patchPwdReq.getUserIdx()};

        return this.jdbcTemplate.update(modfiyPwdQuery,modifyPwdParams);
    }


    // 유저 검증 (jwt 이외에 유저인덱스 검증)
    public int checkUser(int userIdx) {
        String checkUserQuery = "SELECT userIdx FROM User WHERE userIdx = ? AND status = 'ACTIVE'";
        return this.jdbcTemplate.queryForObject(checkUserQuery, int.class, userIdx);

    }

    // 프로필 이미지 변경
    public int modifyUserImage(PatchUserImage patchUserImage){
        String modifyUserImageQuery = "UPDATE User SET imageUrl =? WHERE userIdx = ?";
        Object[] modifyUserImageParams = new Object[]{patchUserImage.getImageUrl(),patchUserImage.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserImageQuery,modifyUserImageParams);
    }

    // 비공개,공개 계정 설정

    public int modifyUserIsPrivate(PatchUserPrivate patchUserPrivate){
        String modifyUserImageQuery = "UPDATE User SET isPrivate =? WHERE userIdx = ?";
        Object[] modifyUserImageParams = new Object[]{patchUserPrivate.getIsPrivate(),patchUserPrivate.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserImageQuery,modifyUserImageParams);
    }


    // 전문분야 조회

    public GetSpecialtyRes getSpecialty(int userIdx) {

        System.out.println("실행됨?");
        // 1. JobGroup
        String getJobGroupQuery = "SELECT EC.categoryIdx,EC.category FROM EmploymentCategory AS EC\n" +
                "JOIN Specialty AS S ON S.categoryIdx = EC.categoryIdx WHERE S.userIdx=?;";

        JobGroup jobGroup = this.jdbcTemplate.queryForObject(getJobGroupQuery,
                (rs, rowNum) -> new JobGroup(
                        rs.getInt("categoryIdx"),
                        rs.getString("category")),
                userIdx);

        // 2. Duty

        String getDutyListQuery = "SELECT ESC.subcategoryIdx,ESC.subcategory FROM EmploymentSubCategory AS ESC\n" +
                "JOIN Specialty AS S ON S.subcategoryIdx = ESC.subcategoryIdx WHERE userIdx = ?;";

        List<Duty> dutyList = this.jdbcTemplate.query(getDutyListQuery,
                (rs, rowNum) -> new Duty(
                        rs.getInt("subcategoryIdx"),
                        rs.getString("subcategory")),
                userIdx);

        // 3. Career

        String getCareerQuery = "SELECT career FROM Specialty WHERE userIdx = ?;";
        int career = this.jdbcTemplate.queryForObject(getCareerQuery,
                (rs,rowNum) -> rs.getInt("career"),
                userIdx);

        // 4. skill

        String getSkillListQuery = "SELECT SK.skillIdx,SK.name FROM Skill AS SK\n" +
                "JOIN SpecialtySkill AS SS ON SS.skillIdx = SK.skillIdx\n" +
                "join Specialty AS S ON S.specialtyIdx = SS.specialtyIdx WHERE S.userIdx =?;";
        List<JobSkill>jobSkillList = this.jdbcTemplate.query(getSkillListQuery,
                (rs,rowNum)-> new JobSkill(
                        rs.getInt("skillIdx"),
                        rs.getString("name")),
                userIdx);

        return new GetSpecialtyRes(jobGroup,dutyList,career,jobSkillList);
    }

    // 전문분야 생성

    //1. 직군 직무 경력 (스킬 제외)

    public int createSpecialty(PostUserSpecialtyReq userSpecialtyReq) throws BaseException {

        String createSpecialtyJobQuery = "INSERT INTO Specialty (categoryIdx,subcategoryIdx,career,userIdx) VALUES (?,?,?,?);";
        Object[] createSpecialtyJobParams = new Object[]{userSpecialtyReq.getJobGroupIdx(),userSpecialtyReq.getDutyIdx(),userSpecialtyReq.getCareer(),userSpecialtyReq.getUserIdx()};
        this.jdbcTemplate.update(createSpecialtyJobQuery, createSpecialtyJobParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int specialtyIdx = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
        createSpecialtySkill(userSpecialtyReq.getJobSkillList(),specialtyIdx);
        return specialtyIdx;

    }

    // 2. 스킬 생성
    public void createSpecialtySkill(List<JobSkill> jobSkillList,int specialtyIdx) throws BaseException {


        String createSpecialtySkillQuery = "INSERT INTO SpecialtySkill (specialtyIdx, skillIdx) VALUES (?,?);";

        for (JobSkill jobSkill : jobSkillList) {
            System.out.println(jobSkill.getSkillIdx());
            Object[] createSpecialtySkillParams = new Object[]{specialtyIdx, jobSkill.getSkillIdx()};
            if (this.jdbcTemplate.update(createSpecialtySkillQuery, createSpecialtySkillParams) == 0) {
                throw new BaseException(UPDATE_FAIL_SPECIALTY_SKILL);
            }
        }
    }

    // 구직여부 설정
    public int modifyJobSearchStatus(PatchJobsStatusReq patchJobsStatusReq) {
        String modifyJobSearchStatusQuery = "UPDATE User SET User.isJobSearch =? WHERE userIdx = ?;";
        Object[] modifyJobSearchStatusParams = new Object[]{patchJobsStatusReq.getIsJobSearch(),patchJobsStatusReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyJobSearchStatusQuery,modifyJobSearchStatusParams);

    }
}
