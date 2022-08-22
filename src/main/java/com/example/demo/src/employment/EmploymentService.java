package com.example.demo.src.employment;

import com.example.demo.src.bookmark.BookmarkDao;
import com.example.demo.src.bookmark.BookmarkProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EmploymentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EmploymentDao employmentDao;
    private final EmploymentProvider employmentProvider;
    private final JwtService jwtService;

    @Autowired
    public EmploymentService(EmploymentDao employmentDao, EmploymentProvider employmentProvider, JwtService jwtService) {
        this.employmentDao = employmentDao;
        this.employmentProvider = employmentProvider;
        this.jwtService = jwtService;
    }
}
