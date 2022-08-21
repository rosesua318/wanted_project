package com.example.demo.src.company;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CompanyService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CompanyDao companyDao;

    private final CompanyProvider companyProvider;
    private final JwtService jwtService;

    @Autowired
    public CompanyService(CompanyDao companyDao, CompanyProvider companyProvider, JwtService jwtService) {
        this.companyDao = companyDao;
        this.companyProvider = companyProvider;
        this.jwtService = jwtService;
    }
}
