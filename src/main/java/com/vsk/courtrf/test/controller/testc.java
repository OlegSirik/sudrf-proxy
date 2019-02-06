package com.vsk.courtrf.test.controller;

import com.vsk.courtrf.court.grabber.Court26MSTerr;
import com.vsk.courtrf.court.repository.CourtRepository;
import com.vsk.courtrf.court.service.CourtService;
import com.vsk.courtrf.court.service.RegionService;
import com.vsk.courtrf.test.service.testsrv;
import com.vsk.courtrf.test.service.testsrvintf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testc {
    @Autowired
    private testsrvintf src;
    @Autowired
    private CourtRepository courtRepo;
    @Autowired
    private CourtService courtSrv;

    /*
    Добавить новое судебное дело для отслеживания
     */
    @GetMapping (path="/test1")
    public String post1() {
        src.addString1("STRING1");
        return "OK";
    }

    @GetMapping (path="/test2")
    public String post2() {
        src.addString2("STRING2");
        return "OK";
    }

    @GetMapping (path="/test26")
    public String post26() {

        Court26MSTerr.updateTerr( courtRepo.findByRegionIdAndCourtType( 26, "MS" ));

        return "OK";
    }

}
