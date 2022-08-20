package com.example.demo.src.company;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CompanyProvider {

    private final CompanyDao companyDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CompanyProvider(CompanyDao companyDao, JwtService jwtService) {
        this.companyDao = companyDao;
        this.jwtService = jwtService;
    }

}
