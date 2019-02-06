package com.vsk.courtrf.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class testsrvImpl implements testsrvintf {

    @Autowired
    private itfAsync as;

    @Override
    public  void addString1(String st) {
        as.addString1("STR##1");
        //as.dummyRun1("STR##1");

    }

    @Override
    public  void addString2(String st) {
        as.addString2("STR##1");
        //as.dummyRun2("STR##2");
    }
}
