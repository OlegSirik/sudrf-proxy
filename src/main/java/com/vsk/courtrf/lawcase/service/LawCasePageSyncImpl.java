package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.court.entity.Region;
import com.vsk.courtrf.court.repository.RegionRepository;
import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;
import com.vsk.courtrf.lawcase.grabber.*;
import com.vsk.courtrf.util.Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class LawCasePageSyncImpl implements LawCasePageSync {

    private static final Logger logger = LoggerFactory.getLogger(LawCasePageSyncImpl.class);

    @Autowired
    private LawCaseDB lawCaseDB;
    @Autowired
    private RegionRepository regionRepo;

    public static Queue<Object> queueSudrf = new ConcurrentLinkedQueue<Object>();
    private static AtomicBoolean runningSudrf = new AtomicBoolean(false);

    public static Queue<Object> queueMossud = new ConcurrentLinkedQueue<Object>();
    private static AtomicBoolean runningMossud = new AtomicBoolean(false);

    public static Queue<Object> queueArbitr = new ConcurrentLinkedQueue<Object>();
    private static AtomicBoolean runningArbitr = new AtomicBoolean(false);


    public void addLawCase4SyncDetails(LawCase lawCase) {

        logger.info( "Add lawCase for sync to queue. ID::" + lawCase.getId());

        if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_SUDRF )) {
            logger.trace( "Add lawCase for sync to queue SUDRF. ID::" + lawCase.getId());
            queueSudrf.add( lawCase );
        } else if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_MOSSUD )) {
            logger.trace( "Add lawCase for sync to queue MOSSUD. ID::" + lawCase.getId());
            queueMossud.add( lawCase );
        } else if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_ARBITR )) {
            logger.trace( "Add lawCase for sync to queue ARBITR. ID::" + lawCase.getId());
            queueArbitr.add( lawCase );
        }
    }

    public void addData4SyncList( LawCaseSearchReq req) {

        if ( req.getCaseSrc().equalsIgnoreCase( LawCase.SRC_SUDRF )) {
            if (Str.stEmpty( req.getRegionCode() ) ) {
                List<Region> regions = regionRepo.findAll();
                for ( Region region: regions ) {
                    LawCaseSearchReq req1 = new LawCaseSearchReq();
                    req1.setSearchWord( req.getSearchWord() );
                    req1.setRegionCode( region.getCode() );
                    req1.setCaseSrc( req.getCaseSrc() );
                    req1.setDaysBack( req.getDaysBack() );
                    queueSudrf.add(req1);
                }
            } else {
                queueSudrf.add(req);
            }
        } else if ( req.getCaseSrc().equalsIgnoreCase( LawCase.SRC_MOSSUD )) {
            queueMossud.add( req );
        } else if ( req.getCaseSrc().equalsIgnoreCase( LawCase.SRC_ARBITR )) {
            queueArbitr.add( req );
        }

    }

    public boolean getRunningSudrf() {
        return runningSudrf.get();
    }

    public boolean getRunningMossud() {
        return runningMossud.get();
    }

    public boolean getRunningArbitr() {
        return runningArbitr.get();
    }

    @Async("processExecutor")
    public void startSyncSudrf() {
        if ( !runningSudrf.get() ) {
            runningSudrf.set(true);
            while ( queueSudrf.size() > 0 ) {
                Object obj = queueSudrf.poll();
                if ( obj instanceof LawCase) {
                    LawCase lc = (LawCase)obj;

                    logger.info("SUDRF::startSyncSudrf::LOOP::ID=" + lc.getId());

                    PageSudrfSinglePage.updateLawCase(20, lc);
                    lawCaseDB.saveLawCase(lc);
                } else if ( obj instanceof LawCaseSearchReq ) {
                    LawCaseSearchReq srcReq = (LawCaseSearchReq)obj;
                    logger.info("SUDRF::startSearchSudrf::LOOP::ID=" + srcReq + "::q.size = " + queueSudrf.size());

                    List<LawCase> lawCases = new ArrayList<LawCase>();
                    PageSudrfCasesSearch.searchBySubjName( srcReq.getRegionCode(), srcReq.getSearchWord(), srcReq.getDaysBack(), 10, lawCases );
                    for (LawCase lc:lawCases) {
                        lc.setCaseSrc( LawCase.SRC_SUDRF );
                    }

                    lawCaseDB.saveLawCaseList( lawCases );
                    lawCases.forEach(item->addLawCase4SyncDetails(item));
                }
            }
        }
        runningSudrf.set(false);
    }

    @Async("processExecutor")
    public void startSyncMossud() {
        if ( !runningMossud.get() ) {
            runningMossud.set(true);
            while ( queueMossud.size() > 0 ) {
                Object obj = queueMossud.poll();
                if ( obj instanceof LawCase) {
                    LawCase lc = (LawCase) obj;
                    logger.info("MOSSUD::startSyncMossud LawCase::ID=" + lc.getId());

                    PageMosSudSinglePage.updateLawCase(2, lc);
                    lawCaseDB.saveLawCase(lc);
                } else if ( obj instanceof LawCaseSearchReq ) {
                    LawCaseSearchReq srcReq = (LawCaseSearchReq)obj;
                    logger.info("MOSSUD::startSearchMossud LawCases::WORD=" + srcReq.getSearchWord() );

                    List<LawCase> lawCases = new ArrayList<LawCase>();
                    PageMosSudCasesSearch.searchBySubjName( srcReq.getSearchWord(), srcReq.getDaysBack(), 2, lawCases );
                    for (LawCase lc:lawCases) {
                        lc.setCaseSrc( LawCase.SRC_MOSSUD );
                    }
                    lawCaseDB.saveLawCaseList( lawCases );

                    lawCases.forEach(item->addLawCase4SyncDetails(item));
                }
            }
        }
        runningMossud.set(false);
    }

    @Async("processExecutor")
    public void startSyncArbitr() {
        logger.info("ARBITR::startSyncArbitr::q.size = " + queueArbitr.size());

        if ( !runningArbitr.get() ) {
            runningArbitr.set(true);
            while ( queueArbitr.size() > 0 ) {
                Object obj = queueArbitr.poll();
                if ( obj instanceof LawCase) {
                    LawCase lc = (LawCase) obj;
                    logger.info("ARBITR::startSyncArbitr::LOOP::ID=" + lc.getId() + "::q.size = " + queueArbitr.size());
                    PageArbitrSinglePage.updateLawCase(100, lc);
                    lawCaseDB.saveLawCase(lc);
                } else if ( obj instanceof LawCaseSearchReq ) {
                    LawCaseSearchReq srcReq = (LawCaseSearchReq)obj;
                    logger.info("ARBITR::startSearchArbitr::LOOP::ID=" + srcReq + "::q.size = " + queueSudrf.size());

                    List<LawCase> lawCases = new ArrayList<LawCase>();
                    PageArbitrCasesSearch.searchBySubjName( srcReq.getSearchWord(), srcReq.getDaysBack(), 30, lawCases );
                    for (LawCase lc:lawCases) {
                        lc.setCaseSrc( LawCase.SRC_ARBITR );
                    }

                    lawCaseDB.saveLawCaseList( lawCases );
                    lawCases.forEach(item->addLawCase4SyncDetails(item));
                }
            }
        }
        runningArbitr.set(false);
    }

}
