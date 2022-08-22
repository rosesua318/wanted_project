package com.example.demo.src.company;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CompanyProvider companyProvider;
    @Autowired
    private final CompanyService companyService;
    @Autowired
    private final CompanyDao companyDao;

    public CompanyController(CompanyProvider companyProvider, CompanyService companyService, CompanyDao companyDao) {
        this.companyProvider = companyProvider;
        this.companyService = companyService;
        this.companyDao = companyDao;
    }




}


