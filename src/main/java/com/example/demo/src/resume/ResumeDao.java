package com.example.demo.src.resume;

import com.example.demo.config.BaseException;
import com.example.demo.src.resume.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class ResumeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetResumeListRes> getResumeList(int userIdx){

        String getResumeListQuery = "SELECT resumeIdx,title,DATE_FORMAT(updatedAt,'%Y.%m.%d') AS updatedAt,\n" +
                "       CASE WHEN status = 'ACTIVE' THEN '작성 완료'\n" +
                "            END AS status,\n" +
                "       CASE WHEN (language) = 0 THEN '한'\n" +
                "            ELSE 'A' END AS language FROM Resume WHERE userIdx = ? AND status = 'ACTIVE';";

        int getResumeListParams = userIdx;

        return this.jdbcTemplate.query(getResumeListQuery,
                (rs,rowNum)-> new GetResumeListRes(
                        rs.getInt("resumeIdx"),
                        rs.getString("title"),
                        rs.getString("updatedAt"),
                        rs.getString("status"),
                        rs.getString("language")),
                getResumeListParams);
    }


    // 이력서 상세 조회

    public GetResumeDetailRes getResumeDetail(int userIdx, int resumeIdx){

        // Resume
        String getResumeIntroQuery = "SELECT resumeIdx,language,title,U.name,U.email,U.phone,introduce,\n" +
                "CASE WHEN language = 0 THEN '한국어'\n" +
                "ELSE '영어' END AS language FROM Resume AS R\n" +
                "JOIN User AS U ON U.userIdx = R.userIdx WHERE R.userIdx = ? AND resumeIdx =?;";

        Object[] getResumeDetailParams = new Object[]{userIdx,resumeIdx};


        Resume resumeIntro = this.jdbcTemplate.queryForObject(getResumeIntroQuery,
                (rs,rowNum)-> new Resume(
                        rs.getInt("resumeIdx"),
                        rs.getString("language"),
                        rs.getString("title"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("introduce")),
                getResumeDetailParams);

        // Career

        String getCareerQuery = "SELECT careerIdx, company, department, isPresent, startedAt, endAt FROM Career AS C\n" +
                "WHERE resumeIdx =? AND C.status = 'ACTIVE';";

        List<Career> careerList = this.jdbcTemplate.query(getCareerQuery,
                (rs,rowNum)-> new Career(
                        rs.getInt("careerIdx"),
                        rs.getString("company"),
                        rs.getString("department"),
                        rs.getInt("isPresent"),
                        rs.getString("startedAt"),
                        rs.getString("endAt")),
                resumeIdx);

        // CareerResult

        String getCareerResultQuery = "SELECT R.careerIdx,R.resultIdx,  R.title, R.startedAt, R.endAt, R.content FROM Result AS R\n" +
                "JOIN Career AS C ON C.careerIdx = R.careerIdx WHERE C.resumeIdx =? AND R.status = 'ACTIVE';";

        List<CareerResult> careerResultList = this.jdbcTemplate.query(getCareerResultQuery,
                (rs,rowNum)-> new CareerResult(
                        rs.getInt("careerIdx"),
                        rs.getInt("resultIdx"),
                        rs.getString("title"),
                        rs.getString("startedAt"),
                        rs.getString("endAt"),
                        rs.getString("content")),
                resumeIdx);

        // Education

        String getEduQuery = "SELECT educationIdx, name AS university, major, study, isPresent, startedAt, endAt FROM Education WHERE resumeIdx = ? AND Education.status = 'ACTIVE';";

        List<Education> educationList = this.jdbcTemplate.query(getEduQuery,
                (rs,rowNum)-> new Education(
                        rs.getInt("educationIdx"),
                        rs.getString("university"),
                        rs.getString("major"),
                        rs.getString("study"),
                        rs.getInt("isPresent"),
                        rs.getString("startedAt"),
                        rs.getString("endAt")),
                resumeIdx);

        // ResumeSkill

        String getResumeSkillQuery = "SELECT RS.skillIdx,S.name AS skill FROM ResumeSkill AS RS\n" +
                "JOIN Skill AS S ON RS.skillIdx = S.skillIdx WHERE RS.resumeIdx = ?;\n";

        List<ResumeSkill> resumeSkillList = this.jdbcTemplate.query(getResumeSkillQuery,
                (rs,rowNum)-> new ResumeSkill(
                        rs.getInt("skillIdx"),
                        rs.getString("skill")),
                resumeIdx);

        // Award

        String getAwardQuery = "SELECT awardsIdx,createdAt,title,content FROM Awards WHERE resumeIdx = ? AND Awards.status = 'ACTIVE';";

        List<Award> awardList = this.jdbcTemplate.query(getAwardQuery,
                (rs,rowNum)-> new Award(
                        rs.getInt("awardsIdx"),
                        rs.getString("createdAt"),
                        rs.getString("title"),
                        rs.getString("content")),
                resumeIdx);


        //ForeignLanguage

        String getFLanguageQuery = "SELECT flIdx, L.nation AS language, level FROM ForeignLanguage AS FL\n" +
                "                            JOIN Language AS L ON FL.languageIdx = L.languageIdx WHERE resumeIdx = ? AND FL.status = 'ACTIVE';";

        List<ForeignLanguage> foreignLanguageList = this.jdbcTemplate.query(getFLanguageQuery,
                (rs,rowNum)-> new ForeignLanguage(
                        rs.getInt("flIdx"),
                        rs.getString("language"),
                        rs.getInt("level")),
                resumeIdx);

        // LanguageTest

        String getTestQuery = "\n" +
                "SELECT T.flIdx ,testIdx,title,score,createdAt FROM Test AS T\n" +
                "JOIN ForeignLanguage AS FL ON FL.flIdx = T.flIdx WHERE FL.resumeIdx = ? AND T.status = 'ACTIVE';";

        List<LanguageTest> languageTestList = this.jdbcTemplate.query(getTestQuery,
                (rs,rowNum)-> new LanguageTest(
                        rs.getInt("flIdx"),
                        rs.getInt("testIdx"),
                        rs.getString("title"),
                        rs.getString("score"),
                        rs.getString("createdAt")),
                resumeIdx);


        // Link

        String getLinkQuery = "SELECT linkIdx,url AS linkUrl FROM Link WHERE resumeIdx = ? AND Link.status = 'ACTIVE' ;";

        List<ResumeLink> resumeLinkList = this.jdbcTemplate.query(getLinkQuery,
                (rs,rowNum) -> new ResumeLink(
                        rs.getInt("linkIdx"),
                        rs.getString("linkUrl")),
                resumeIdx);


        return new GetResumeDetailRes(resumeIntro,careerList,careerResultList,educationList,resumeSkillList,awardList,foreignLanguageList,languageTestList,resumeLinkList);

    }

    // 이력서 생성

    public int createResume(int userIdx){

        String getTitleQuery ="SELECT CONCAT((SELECT name FROM User WHERE User.userIdx =?),(SELECT COUNT(resumeIdx)+1 FROM Resume WHERE userIdx = ? AND Resume.status = 'ACTIVE')) AS title;";

        Object[] createTitleParams = new Object[]{userIdx,userIdx};

        String title = this.jdbcTemplate.queryForObject(getTitleQuery,
                (rs,rowNum)-> new String(
                        rs.getString("title")),
                createTitleParams);

        String createResumeQuery = "INSERT INTO Resume(Resume.userIdx,title) VALUES (?,?);";
        Object[] createResumeParams = new Object[]{userIdx,title};
        this.jdbcTemplate.update(createResumeQuery, createResumeParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);


    }

    // 이력서 각 테이블(요소) 생성(empty)

    public int createResumeTable(ResumeTable resumeTable,int Idx) throws BaseException{


        String tableName = resumeTable.getTableName();
        String keyName = resumeTable.getParentKey();

        String createResumeTableQuery = " INSERT INTO " + tableName + " (" + keyName + ") VALUES (?);" ;

        if(this.jdbcTemplate.update(createResumeTableQuery,Idx)==0){
            throw new BaseException(CREATE_FAIL_RESUMETABLE);
        }
        String lastInsertQuery = "SELECT last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
    }


    // 이력서 각 테이블(요소) 삭제하기

    public int deleteResumeTable(ResumeTable resumeTable, int Idx) throws BaseException {
        String tableName = resumeTable.getTableName();
        String primaryKey = resumeTable.getPrimaryKey();
        String deleteResumeTableQuery = " UPDATE " + tableName + " SET status = 'DELETE' WHERE " + primaryKey + " = ?";
        return this.jdbcTemplate.update(deleteResumeTableQuery,Idx);
    }


    public int checkUser(int resumeIdx) {
        String checkUserQuery = "SELECT userIdx FROM Resume WHERE resumeIdx = ?";
        return this.jdbcTemplate.queryForObject(checkUserQuery, int.class, resumeIdx);

    }

    // 이력서 작성/수정

    //1.ResumeInfo
    public void updateResumeInfo(Resume resumeIntro, int resumeIdx) throws BaseException{
        String updateResumeInfoQuery = " update Resume set language = ?,title = ?, introduce =?  WHERE resumeIdx =? ";
        Object[] updateResumeInfoParams = new Object[]{resumeIntro.getLanguage(),resumeIntro.getTitle(),resumeIntro.getIntroduce(),resumeIntro.getResumeIdx()};
        if(this.jdbcTemplate.update(updateResumeInfoQuery, updateResumeInfoParams)==0){
           throw new BaseException(UPDATE_FAIL_RESUMEINFO);
       }

    }
    // 2. Career
    public void updateCareer(List<Career> careerList) throws BaseException{
        String updateCareerQuery = "UPDATE Career SET company = ?, department = ? , isPresent =?, startedAt =?, endAt =?  WHERE careerIdx =?;";

        for(Career career : careerList ) {
            Object[] updateCareerParams = new Object[]{career.getCompany(), career.getDepartment(), career.getIsPresent(), career.getStartedAt(), career.getEndAt(), career.getCareerIdx()};
            if (this.jdbcTemplate.update(updateCareerQuery, updateCareerParams) == 0) {
                throw new BaseException(UPDATE_FAIL_CAREER);
            }
        }
    }

    // 3. CareerResult

    public void updateCareerResult(List<CareerResult> careerResultList) throws BaseException{
        String updateCareerResultQuery = "UPDATE Result SET title =?, startedAt =?, endAt =?, content =? WHERE resultIdx = ?;";
        for(CareerResult cr : careerResultList){
            Object[] updateCareerResultParams = new Object[]{cr.getTitle(),cr.getStartedAt(),cr.getEndAt(),cr.getContent(),cr.getResultIdx()};
            if(this.jdbcTemplate.update(updateCareerResultQuery, updateCareerResultParams) == 0) {
                throw new BaseException(UPDATE_FAIL_CAREERESULT);
            }
        }
    }

    // 4. Education
    public void updateEducation(List<Education> educationList) throws BaseException{
        String updateEducationQuery = "UPDATE Education SET name =?, major =?, study =?, isPresent =?, startedAt =?, endAt =? WHERE educationIdx = ?;";

        for( Education edu : educationList){
                Object[] updateEducationParams = new Object[]{edu.getUniversity(),edu.getMajor(),edu.getStudy(),edu.getIsPresent(),edu.getStartedAt(),edu.getEndAt(),edu.getEduIdx()};
                if(this.jdbcTemplate.update(updateEducationQuery, updateEducationParams) == 0) {
                    throw new BaseException(UPDATE_FAIL_EDUCATION);
                 }
         }
    }

    // 5. Award
    public void updateAward(List<Award> awardList) throws BaseException{
        String updateAwardQuery = "UPDATE Awards SET createdAt =?, title =?, content =? WHERE awardsIdx = ?;";

        for(Award award : awardList) {
            Object[] updateAwardParams = new Object[]{award.getCreatedAt(), award.getTitle(), award.getContent(), award.getAwardIdx()};
            if (this.jdbcTemplate.update(updateAwardQuery, updateAwardParams) == 0) {
                throw new BaseException(UPDATE_FAIL_AWARD);
            }
        }
    }

    //6. ForeignLanguage
    public void updateLanguage(List<ForeignLanguage> foreignLanguageList) throws BaseException{
        String updateLanguageQuery = "UPDATE ForeignLanguage SET languageIdx =?, level =? WHERE flIdx = ?;";
        for(ForeignLanguage fl : foreignLanguageList){
            Object[] updateLanguageParams = new Object[]{fl.getLanguage(),fl.getLevel(),fl.getForeignLngIdx()};
            if(this.jdbcTemplate.update(updateLanguageQuery, updateLanguageParams) == 0) {
            throw new BaseException(UPDATE_FAIL_LANGUAGE);
            }
        }
    }

    // 7. ForeignLanguage Test
    public void updateLanguageTest(List<LanguageTest> languageTestList) throws BaseException{
        String updateTestQuery = "UPDATE Test SET title =?, score =?, createdAt =? WHERE testIdx = ?;";

        for(LanguageTest lt : languageTestList) {
            Object[] updateTestParams = new Object[]{lt.getTitle(), lt.getScore(), lt.getCreatedAt(), lt.getLanguageTestIdx()};
            if (this.jdbcTemplate.update(updateTestQuery, updateTestParams) == 0) {
                throw new BaseException(UPDATE_FAIL_TEST);
            }
        }
    }

    // 8. Link
    public void updateLink(List<ResumeLink> linkList) throws BaseException {
        String updateLinkQuery = "UPDATE Link SET url =? WHERE linkIdx = ?;";

        for (ResumeLink link : linkList) {
            Object[] updateLinkParams = new Object[]{link.getLinkUrl(), link.getLinkIdx()};
            if (this.jdbcTemplate.update(updateLinkQuery, updateLinkParams) == 0) {
                throw new BaseException(UPDATE_FAIL_LINK);
            }
        }
    }

    // 이력서 삭제
    public int deleteResume(int resumeIdx) throws BaseException {
        String deleteResumeQuery = " UPDATE Resume SET status = 'DELETE' WHERE resumeIdx = ?";
        return this.jdbcTemplate.update(deleteResumeQuery,resumeIdx);
    }

    // 해당 인덱스의 이력서가 있는지 확인합니다.
//    public int checkResume(int resumeIdx) throws BaseException{
//        String checkResumeQuery = "SELECT EXISTS(SELECT resumeIdx FROM Resume WHERE resumeIdx = ?)";
//        System.out.println("checkResume");
//        int checkResumeParams = resumeIdx;
//        return this.jdbcTemplate.queryForObject(checkResumeQuery,
//                int.class,
//                checkResumeParams);
//    }
}
