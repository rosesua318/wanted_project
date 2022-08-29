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
     * 북마크 중복 검사 (이미 북마크 한 경우)
     */
    public int checkBookmark(int employmentIdx, int userIdx){
        String checkBookmmarkQuery = "SELECT EXISTS(SELECT employmentIdx from Bookmark where employmentIdx = ? and userIdx = ? and status = 'ACTIVE')";
        Object[] checkBookmarkParams = new Object[]{employmentIdx,userIdx};
        return this.jdbcTemplate.queryForObject(checkBookmmarkQuery,int.class,
                checkBookmarkParams);
    }

    /**   북마크 검사 -> DB 상 삭제로 되어있을 경우 STATUS 만 바꾸기
     *
     */
    public int checkBookmarkDelete(int employmentIdx){
        String checkBookmmarkQuery = "SELECT EXISTS(SELECT employmentIdx from Bookmark where employmentIdx = ? and status = 'DELETE')";
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
    public int DeleteBookmarkStatus(int employmentIdx, int userIdx) throws BaseException{
        String modifyBookmarkStatusQuery = "UPDATE Bookmark SET status ='DELETE' WHERE employmentIdx = ? AND userIdx = ?";
        Object[] modifyBookmarkStatusParams = new Object[]{employmentIdx,userIdx};

        return this.jdbcTemplate.update(modifyBookmarkStatusQuery,modifyBookmarkStatusParams);
    }

    public int ActiveBookmarkStatus(int employmentIdx, int userIdx) throws BaseException{
        String modifyBookmarkStatusQuery = "UPDATE Bookmark SET status ='ACTIVE' WHERE employmentIdx = ? AND userIdx = ?";
        Object[] modifyBookmarkStatusParams = new Object[]{employmentIdx,userIdx};

        return this.jdbcTemplate.update(modifyBookmarkStatusQuery,modifyBookmarkStatusParams);
    }
}
