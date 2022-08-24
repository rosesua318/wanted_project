package com.example.demo.src.alarm;

import com.example.demo.config.BaseException;
import com.example.demo.src.alarm.model.Alarm;
import com.example.demo.src.alarm.model.AlarmSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class AlarmProvider {

    private final AlarmDao alarmDao;

    public AlarmProvider(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public List<Alarm> getAlarms(int userIdx) throws BaseException {
        try {
            return alarmDao.getAlarms(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public AlarmSetting getAlarmSetting(int userIdx) throws BaseException {
        try {
            return alarmDao.getAlarmSetting(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
