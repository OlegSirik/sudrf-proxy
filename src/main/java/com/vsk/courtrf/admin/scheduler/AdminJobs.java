package com.vsk.courtrf.admin.scheduler;

import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;
import com.vsk.courtrf.lawcase.repository.LawCaseSearchRepo;
import com.vsk.courtrf.lawcase.service.LawCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminJobs {

    @Autowired
    private LawCaseSearchRepo lcSearchRepo;
    @Autowired
    private LawCaseService lcService;


    @Scheduled(cron = "5 22 * * * *")
    public void updateAllSearchWords() {
        Iterable<LawCaseSearchReq> lcList = lcSearchRepo.findAll();
        for ( LawCaseSearchReq rc: lcList ) {
            lcService.addSearchPattern( rc );
        }
    }


    @Scheduled(cron = "5 23 * * * *")
    public void syncAllCases() {
        System.out.println( "CRON TASK - " + System.currentTimeMillis() / 1000);
        lcService.syncAllCases();
    }



}
