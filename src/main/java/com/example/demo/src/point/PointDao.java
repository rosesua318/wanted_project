package com.example.demo.src.point;

import com.example.demo.src.point.model.GetPointRes;
import com.example.demo.src.point.model.MonthPoint;
import com.example.demo.src.point.model.PointRecord;
import com.example.demo.src.wanted.model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PointDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetPointRes getPoints(int userIdx) {
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

        String getDestroyPointQuery = "select p.isPlus, p.cost from User u " +
                "JOIN Point p ON p.userIdx = u.userIdx " +
                "where u.userIdx=? and datediff(p.endAt, now()) <= 7";
        String getDestroyPointParams = String.valueOf(userIdx);
        List<Point> destroyPoints = this.jdbcTemplate.query(getDestroyPointQuery,
                (rs, rowNum) -> new Point(
                        rs.getInt("isPlus"),
                        rs.getInt("cost")),
                getDestroyPointParams);

        int destroyPoint = 0;

        for(Point p : destroyPoints) {
            if(p.getIsPlus() == 1) {
                destroyPoint += p.getCost();
            } else {
                destroyPoint -= p.getCost();
            }
        }

        if(destroyPoint < 0) {
            destroyPoint = 0;
        }

        String getMonthQuery = "select date_format(p.createdAt, '%Y.%m') month FROM Point p GROUP BY month";
        List<MonthPoint> months = this.jdbcTemplate.query(getMonthQuery,
                (rs, rowNum) -> new MonthPoint(
                        rs.getString("month")));

        for(MonthPoint m : months) {
            String getRecordQuery = "select p.pointIdx, date_format(p.createdAt, '%m.%d') date, p.title, p.isPlus, p.cost, date_format(p.endAt, '%y.%m.%d') endAt " +
                    "from Point p where date_format(p.createdAt, '%Y.%m') LIKE ?";
            String getRecordParams = m.getMonth();
            List<PointRecord> pointList = this.jdbcTemplate.query(getRecordQuery,
                    (rs, rowNum) -> new PointRecord(
                            rs.getInt("pointIdx"),
                            rs.getString("date"),
                            rs.getString("title"),
                            rs.getString("isPlus"),
                            rs.getInt("cost"),
                            rs.getString("endAt")),
                    getRecordParams);

            for(PointRecord p : pointList) {
                if(p.getIsPlus().equals("1")) {
                    p.setIsPlus("+");
                } else {
                    p.setIsPlus("-");
                }
            }
            m.setPoints(pointList);
        }

        return new GetPointRes(point, destroyPoint, months);
    }
}
