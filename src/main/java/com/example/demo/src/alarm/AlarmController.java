package com.example.demo.src.alarm;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.alarm.model.Alarm;
import com.example.demo.src.alarm.model.AlarmSetting;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/alarms")
public class AlarmController {

    @Autowired
    private final AlarmProvider alarmProvider;

    @Autowired
    private final AlarmService alarmService;

    @Autowired
    private final JwtService jwtService;

    public AlarmController(AlarmProvider alarmProvider, AlarmService alarmService, JwtService jwtService) {
        this.alarmProvider = alarmProvider;
        this.alarmService = alarmService;
        this.jwtService = jwtService;
    }

    /**
     * 알림 조회 API
     * [GET] /alarms/records/:userIdx
     * @return BaseResponse<List<Alarm>>
     */
    @ResponseBody
    @GetMapping("/records/{userIdx}")
    public BaseResponse<List<Alarm>> getAlarms(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            List<Alarm> alarms = alarmProvider.getAlarms(userIdx);
            return new BaseResponse<>(alarms);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 알림 설정 조회 API
     * [GET] /alarms/:userIdx
     * @return BaseResponse<AlarmSetting>
     */
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<AlarmSetting> getAlarmSetting(@PathVariable("userIdx") int userIdx) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            AlarmSetting alarmSetting = alarmProvider.getAlarmSetting(userIdx);
            return new BaseResponse<>(alarmSetting);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 알림 설정 API
     * [PATCH] /alarms/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> setAlarm(@PathVariable("userIdx") int userIdx, @RequestBody AlarmSetting alarmSetting) throws BaseException {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            alarmService.setAlarm(userIdx, alarmSetting);
            return new BaseResponse<>("설정 변경 되었습니다.");
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }
}
