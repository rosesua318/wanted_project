package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.model.GetBookmarkEmpIdRes;
import com.example.demo.src.bookmark.model.PatchBookmarkStatusReq;
import com.example.demo.src.bookmark.model.PostBookMarkReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BookmarkDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     *
     * 북마크 생성 API
     */
    public int createBookmark(PostBookMarkReq postBookMarkReq) throws BaseException {

        String createDibsQuery = "INSERT INTO Bookmark (employmentIdx,userIdx) VALUES (?,?)";
        Object[] createDibsParams = new Object[]{postBookMarkReq.getEmploymentIdx(),postBookMarkReq.getUserIdx()};

        this.jdbcTemplate.update(createDibsQuery, createDibsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

    }
    /**
     * 북마크 중복 검사
     */
    public int checkBookmark(int employmentIdx){
        String checkBookmmarkQuery = "SELECT EXISTS(SELECT employmentIdx from Bookmark where employmentIdx = ?)";
        int checkBookmarkParams = employmentIdx;
        return this.jdbcTemplate.queryForObject(checkBookmmarkQuery,int.class,
                checkBookmarkParams);
    }

    /**
     * 북마크 관련 채용 공고 포지션 추출
     */
    public List<GetBookmarkEmpIdRes> getEmploymentId(int userIdx) {

        String getEmploymentIdQuery = "select employmentIdx from Bookmark where userIdx = ?";
        int getStoreIdParam = userIdx;

        return this.jdbcTemplate.query(getEmploymentIdQuery,
                ((rs, rowNum) -> new GetBookmarkEmpIdRes(
                        rs.getInt("employmentIdx")
                ))
                ,getStoreIdParam);
    }



    public int modifyBookmarkStatus(PatchBookmarkStatusReq patchBookmarkStatusReq){
        String modifyCartStatusQuery = "UPDATE Bookmark SET status ='DELETE' WHERE bookmarkIdx = ?";
        Object[] modifyCartStatusParams = new Object[]{patchBookmarkStatusReq.getBookmarkIdx()};

        return this.jdbcTemplate.update(modifyCartStatusQuery,modifyCartStatusParams);

    }



}
