package com.example.demo.src.resume;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResumeDao resumeDao;

    private final ResumeProvider resumeProvider;

    private final JwtService jwtService;

    @Autowired
    public ResumeService(ResumeDao resumeDao, ResumeProvider resumeProvider, JwtService jwtService) {
        this.resumeDao = resumeDao;
        this.resumeProvider = resumeProvider;
        this.jwtService = jwtService;
    }
}
