package com.example.demo.src.alarm;

import com.example.demo.config.BaseException;
import com.example.demo.src.alarm.model.AlarmSetting;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional
public class AlarmService {

    private final AlarmDao alarmDao;


    public AlarmService(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    public void setAlarm(int userIdx, AlarmSetting alarmSetting) throws BaseException {
        try {
            alarmDao.setAlarm(userIdx, alarmSetting);
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
