package com.example.demo.src.alarm;

import com.example.demo.config.BaseException;
import com.example.demo.src.alarm.model.Alarm;
import com.example.demo.src.alarm.model.AlarmSetting;
import com.example.demo.src.like.model.GetNationRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.SET_ALARM_WRONG_NUMBER;

@Repository
public class AlarmDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<Alarm> getAlarms(int userIdx) {
        String getAlarmQuery = "select a.alarmIdx, a.type, a.content, " +
                "date_format(a.createdAt, '%Y.%m.%d ') date from Alarm a JOIN User u" +
                " ON a.userIdx = u.userIdx where u.userIdx = ?";
        String getAlarmParams = String.valueOf(userIdx);

        List<Alarm> alarms = this.jdbcTemplate.query(getAlarmQuery,
                (rs, rowNum) -> new Alarm(
                        rs.getInt("alarmIdx"),
                        rs.getString("type"),
                        rs.getString("content"),
                        rs.getString("date")),
                getAlarmParams);

        for(Alarm a : alarms) {
            String getDayQuery = "select date_format(a.createdAt, '%w') day from Alarm a where a.alarmIdx = ?";
            String getDayParams = String.valueOf(a.getAlarmIdx());

            String day = this.jdbcTemplate.queryForObject(getDayQuery,
                    (rs, rowNum) -> new String(
                            rs.getString("day")),
                    getDayParams);

            if(day.equals("0")) {
                a.setDate(a.getDate() + "(일)");
            } else if(day.equals("1")) {
                a.setDate(a.getDate() + "(월)");
            } else if(day.equals("2")) {
                a.setDate(a.getDate() + "(화)");
            } else if(day.equals("3")) {
                a.setDate(a.getDate() + "(수)");
            } else if(day.equals("4")) {
                a.setDate(a.getDate() + "(목)");
            } else if(day.equals("5")) {
                a.setDate(a.getDate() + "(금)");
            } else if(day.equals("6")) {
                a.setDate(a.getDate() + "(토)");
            }
        }
        return alarms;
    }

    public AlarmSetting getAlarmSetting(int userIdx) {
        String getSettingQuery = "select isEvent, isRecommend from User where userIdx = ?";
        String getSettingParams = String.valueOf(userIdx);

        return this.jdbcTemplate.queryForObject(getSettingQuery,
                (rs, rowNum) -> new AlarmSetting(
                        rs.getInt("isEvent"),
                        rs.getInt("isRecommend")),
                getSettingParams);
    }

    public void setAlarm(int userIdx, AlarmSetting alarmSetting) throws BaseException {
        if(alarmSetting.getIsEvent() != 0 && alarmSetting.getIsEvent() != 1) {
            throw new BaseException(SET_ALARM_WRONG_NUMBER);
        }
        if(alarmSetting.getIsRecommend() != 0 && alarmSetting.getIsRecommend() != 1) {
            throw new BaseException(SET_ALARM_WRONG_NUMBER);
        }

        String setAlarmQuery = "update User set isEvent = ?, isRecommend = ? where userIdx = ?";
        Object[] setAlarmParams = new Object[]{alarmSetting.getIsEvent(), alarmSetting.getIsRecommend(), userIdx};
        this.jdbcTemplate.update(setAlarmQuery, setAlarmParams);
    }
}
