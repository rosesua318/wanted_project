package com.example.demo.src.position;


import com.example.demo.config.BaseException;
import com.example.demo.src.position.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class PositionProvider {
    private final PositionDao positionDao;

    private final JwtService jwtService;

    public PositionProvider(PositionDao positionDao, JwtService jwtService) {
        this.positionDao = positionDao;
        this.jwtService = jwtService;
    }

    public GetOpenPositionRes getPositionOpen(int jobIdx, int dutyIdx) throws BaseException {
        try {
            GetOpenPositionRes getOpenPositionRes = positionDao.getPositionOpen(jobIdx, dutyIdx);
            return getOpenPositionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetOpenPositionRes getPosition(int userIdx, int jobIdx, int dutyIdx) throws BaseException {
        try {
            GetOpenPositionRes getOpenPositionRes = positionDao.getPosition(userIdx, jobIdx, dutyIdx);
            return getOpenPositionRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<JobCategory> getJobCategory() throws BaseException {
        try {
            List<JobCategory> jobCategories = positionDao.getJobCategory();
            return jobCategories;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<DutyCategory> getDutyCategory(int jobIdx) throws BaseException {
        try {
            List<DutyCategory> dutyCategories = positionDao.getDutyCategory(jobIdx);
            return dutyCategories;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Nation> getNations() throws BaseException {
        try {
            List<Nation> nations = positionDao.getNations();
            return nations;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<Region> getRegions(int nationIdx) throws BaseException {
        try {
            List<Region> regions = positionDao.getRegions(nationIdx);
            return regions;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<DetailRegion> getDetailRegions(int regionIdx) throws BaseException {
        try {
            List<DetailRegion> detailRegions = positionDao.getDetailRegions(regionIdx);
            return detailRegions;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<EmpStack> getStacks() throws BaseException {
        try {
            List<EmpStack> stacks = positionDao.getStacks();
            return stacks;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
