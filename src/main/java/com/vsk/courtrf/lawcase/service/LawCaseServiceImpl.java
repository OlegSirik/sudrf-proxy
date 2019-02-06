package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.lawcase.controller.request.AddLawCase;
import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseCalendar;
import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;
import com.vsk.courtrf.lawcase.repository.LawCaseCalendarRepo;
import com.vsk.courtrf.lawcase.repository.LawCaseDetailsRepository;
import com.vsk.courtrf.lawcase.repository.LawCaseRepository;
import com.vsk.courtrf.lawcase.repository.LawCaseSearchRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LawCaseServiceImpl implements LawCaseService {

    private static final Logger logger = LoggerFactory.getLogger(LawCaseServiceImpl.class);

    @Autowired
    private LawCaseRepository lawCaseRepo;
    @Autowired
    private LawCaseDetailsRepository lawCaseDetailsRepo;
    @Autowired
    private LawCaseCalendarRepo lawCaseCalendarRepo;
    @Autowired
    private LawCaseSearchRepo lcSearchRepo;

    @Autowired
    private LawCasePageSync lcSync;

    @Override
    public LawCase findById(Long id) {
        LawCase lawCase = lawCaseRepo.findById(id);
        if ( lawCase.getCaseSrc().equalsIgnoreCase(LawCase.SRC_ARBITR) ) {
            lawCase.setDetails( lawCaseDetailsRepo.findByCaseIdAndPageNr(id, 1));
        } else {
            lawCase.setCalendar(lawCaseCalendarRepo.findByCaseId(id));
        }
        return lawCase;
    }

    @Override
    // Поиск по локальной БД
    public List<LawCase> findByUpdateFlag(String flag) {
        return lawCaseRepo.findByUpdateFlag( LawCase.UPDATE_MAKE );
    }

    public List<LawCase> findByUpdateDate(Date date) {
        Date dt = date;

        List<LawCase> lcList = lawCaseRepo.findByDateUpdateAfter(date);
        for ( LawCase lc: lcList ) {
            List<LawCaseCalendar> calendar = lawCaseCalendarRepo.findByCaseIdAndDateUpdateAfter (lc.getId(), date);
            lc.setCalendar( calendar );
        }

        return lcList;
    }

    @Override
    public void addSearchPattern(LawCaseSearchReq req) {
        lcSearchRepo.save(req);
        lcSync.addData4SyncList( req );
        startSyncThread( req.getCaseSrc() );
    }

    @Override
    public List<LawCaseSearchReq> findAllSearchPatterns() {
        List<LawCaseSearchReq> list = new ArrayList<LawCaseSearchReq>();
        lcSearchRepo.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void removeSearchPattern(Integer id) {
        lcSearchRepo.deleteById( id );
    }


    @Override
    // Обновление всех дел из БД по
    public void syncAllCases() {
        List<LawCase> lawCases = lawCaseRepo.findCasesForSync();
        lawCases.forEach(item->reloadLawCaseDetailsFromSite(item));
    }

    @Override
    public void syncOneCase(Long id) {
        LawCase lawCase = lawCaseRepo.findById(id);
        reloadLawCaseDetailsFromSite( lawCase );
    }

    private String getArbitrCode(String href) {
        int i1 = href.lastIndexOf("/");
        String st = href.substring( i1+1 );
        return st;
    }

    @Override
    // Добавить дела в БД для отслеживания
    public LawCase addLawCase(AddLawCase lawCase) {
        LawCase lc = new LawCase();
        lc.setCaseHref( lawCase.getHref());
        if ( lc.getCaseHref().indexOf("mos-gorsud") > 0 ) {
            lc.setCaseRegion("77");
            lc.setCaseSrc( LawCase.SRC_MOSSUD );
        } else if ( lc.getCaseHref().indexOf(".arbitr.") > 0 ) {
            lc.setCaseSrc( LawCase.SRC_ARBITR );
            lc.setCaseCode( getArbitrCode(lc.getCaseHref()) );
        } else {
            lc.setCaseRegion("00");
            lc.setCaseSrc( LawCase.SRC_SUDRF );
        }

        lawCaseRepo.save(lc);
        reloadLawCaseDetailsFromSite( lc );

        return findById( lc.getId() );
    }

    private void reloadLawCaseDetailsFromSite(LawCase lawCase) {
        System.out.println("ADD LAW CASE ID# " + lawCase.getId());
        lcSync.addLawCase4SyncDetails( lawCase );
        startSyncThread( lawCase.getCaseSrc() );
    }

    private void startSyncThread( String src ){
        if ( src.equalsIgnoreCase( LawCase.SRC_SUDRF )) {
            if ( !lcSync.getRunningSudrf()) { lcSync.startSyncSudrf(); }
        } else if ( src.equalsIgnoreCase( LawCase.SRC_MOSSUD )) {
            boolean bl = lcSync.getRunningMossud();
            if ( !lcSync.getRunningMossud()) { lcSync.startSyncMossud(); }
        } else if ( src.equalsIgnoreCase( LawCase.SRC_ARBITR )) {
            if ( !lcSync.getRunningArbitr() ) { lcSync.startSyncArbitr(); }
        }
    }



}
