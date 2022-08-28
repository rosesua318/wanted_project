package com.example.demo.src.bookmark;

import com.example.demo.config.BaseException;
import com.example.demo.src.bookmark.model.Bookmark;
import com.example.demo.src.bookmark.model.GetBookmarkEmpIdRes;

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
    public int createBookmark(Bookmark.Request request) throws BaseException {

        String createDibsQuery = "INSERT INTO Bookmark (employmentIdx,userIdx) VALUES (?,?)";
        Object[] createDibsParams = new Object[]{request.getEmploymentIdx(),request.getUserIdx()};

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

    /**
     * 북마크 삭제
     */
    public int modifyBookmarkStatus(Bookmark.BookmarkStatus bookmarkStatusReq){
        String modifyCartStatusQuery = "UPDATE Bookmark SET status ='DELETE' WHERE bookmarkIdx = ?";
        Object[] modifyCartStatusParams = new Object[]{bookmarkStatusReq.getBookmarkIdx()};

        return this.jdbcTemplate.update(modifyCartStatusQuery,modifyCartStatusParams);
    }
}
