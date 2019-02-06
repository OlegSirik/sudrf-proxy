package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseCalendar;
import com.vsk.courtrf.lawcase.entity.LawCaseDetails;
import com.vsk.courtrf.lawcase.repository.LawCaseCalendarRepo;
import com.vsk.courtrf.lawcase.repository.LawCaseDetailsRepository;
import com.vsk.courtrf.lawcase.repository.LawCaseRepository;
import com.vsk.courtrf.util.Dt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class LawCaseDBImpl implements LawCaseDB {

    private static final Logger logger = LoggerFactory.getLogger(LawCaseDBImpl.class);

    @Autowired
    private LawCaseRepository lawCaseRepo;
    @Autowired
    private LawCaseDetailsRepository lawCaseDetailsRepo;
    @Autowired
    private LawCaseCalendarRepo lawCaseCalendarRepo;


    public static void main(String[] args) {
        LawCase lc = new LawCase();
        lc.setCaseSrc(LawCase.SRC_SUDRF);
        lc.setCaseInfo("КАТЕГОРИЯ: 2.169 - О защите прав потребителей -> из договоров в сфере торговли, услуг... -> иные договоры в сфере услуг ИСТЕЦ(ЗАЯВИТЕЛЬ): Беспалова А. В. ОТВЕТЧИК: ООО \"Группа Ренессанс Страхование\"");
        lc = PreSaveSudrf(lc);
    }


    private HashMap<String, LawCaseCalendar> getDBCalendar(Long case_id) {
        HashMap<String, LawCaseCalendar> hMap = new HashMap();
        List<LawCaseCalendar> calendar = lawCaseCalendarRepo.findByCaseId(case_id);
        for (LawCaseCalendar rc: calendar) {
            hMap.put( rc.hashKey(), rc);
        }
        return hMap;
    }
    private LawCaseCalendar convert2calendar(LawCaseDetails details){

        if ( details.getPageNr() == 2) {
            LawCaseCalendar calendar;
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date eventDate = simpleDateFormat.parse(details.getCol2());
                calendar = new LawCaseCalendar(details.getCaseId(), details.getCol1(), eventDate, details.getCol3());
                calendar.setLastError("");
            } catch (ParseException e) {
                calendar = new LawCaseCalendar(details.getCaseId(), details.getCol1(), new Date(), details.getCol3());
                calendar.setLastError(e.toString());
            }

            calendar.setEventResult( details.getCol4() );
            calendar.setEventReason( details.getCol5() );
            return calendar;
        }
        return null;
    }

    // сохранение данных в БД со всеми доп проверками
    public void saveLawCase(LawCase lawCase) {
        boolean calendarChanged = false;

        lawCase.setDateSync( new Date());

        if ( lawCase.getCaseSrc().equalsIgnoreCase(LawCase.SRC_MOSSUD)) {
            lawCase = PreSaveMossud(lawCase);
        } else if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_SUDRF )) {
            lawCase = PreSaveSudrf(lawCase);
        }

        if ( lawCase.getId() == null ) {
            logger.info( "Insert new Law case");
            if ( checkLawCaseExists ( lawCase ) ) {
                logger.info( " LawCase already exists in DB ");
                return;
            }
            lawCase.setDateInsert( Dt.truncToday() );
            lawCase.setDateUpdate( Dt.truncToday() );
            lawCase.setUpdateFlag( LawCase.UPDATE_MAKE );

            lawCaseRepo.save(lawCase);
        } else {
            LawCase currCase = lawCaseRepo.findById( lawCase.getId()) ;
            if ( !lawCase.equals( currCase ) ) {
                lawCase.setDateUpdate( Dt.truncToday() );
                lawCaseRepo.save( lawCase );

                logger.info( "Update Law case; ID = " + lawCase.getId());
            } else {
                /* update sync date */
                lawCaseRepo.save( lawCase );
                logger.info( "No changes Law case; ID = " + lawCase.getId());
            }
        }
        // детали  так сохраняем/
        Long c1 = lawCase.getId();
        List<LawCaseDetails> c2 = lawCaseDetailsRepo.findByCaseId( c1 );
        lawCaseDetailsRepo.deleteAll( c2 );
        lawCaseDetailsRepo.saveAll( lawCase.getDetails() );

        // календарь из БД
        HashMap<String, LawCaseCalendar> currCalendar = getDBCalendar(lawCase.getId());
        for (LawCaseDetails rc: lawCase.getDetails()) {
            if ( rc.getPageNr() == 2) {
                LawCaseCalendar newRec = new LawCaseCalendar();
                newRec = convert2calendar(rc);

                LawCaseCalendar dbRec = currCalendar.get( newRec.hashKey() );
                if ( dbRec == null ) {
                    // Это новая запись
                    newRec.setDateInsert( Dt.truncToday());
                    newRec.setDateUpdate( Dt.truncToday());
                    lawCaseCalendarRepo.save( newRec );
                    calendarChanged = true;
                } else {
                    if ( !dbRec.equals( newRec )) {
                        // Существующая запись изменилась
                        dbRec.setDateUpdate( Dt.truncToday() );
                        dbRec.setEventResult( newRec.getEventResult() );
                        lawCaseCalendarRepo.save( dbRec );
                        calendarChanged = true;
                    }
                }
            }
        }

        if ( calendarChanged ) {
            /* Календарь изменился */
            if ( lawCase.getDateUpdate() != Dt.truncToday()) {
                /* если при этом не изменилось само дело, то поменять дату последнего изменения дела */
                lawCase.setDateUpdate( Dt.truncToday() );
                lawCaseRepo.save( lawCase );
            }
        }
    }

    public void saveLawCaseList(List<LawCase> lawCases) {
        for ( LawCase lc:lawCases ) {
                saveLawCase(lc);
        }
    }

    private boolean checkLawCaseExists(LawCase lawCase) {
        if ( lawCase.getId() != null ) { return true; }

        if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_ARBITR )) {
            List<LawCase> lcList = lawCaseRepo.findByCaseHref( lawCase.getCaseHref() );
            if ( lcList.size() > 0 ) { return true; }
        } else if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_SUDRF )) {
            List<LawCase> lcList = lawCaseRepo.findByCaseHref( lawCase.getCaseHref() );
            if ( lcList.size() > 0 ) { return true; }
        } else if ( lawCase.getCaseSrc().equalsIgnoreCase( LawCase.SRC_MOSSUD )) {
            List<LawCase> lcList = lawCaseRepo.findByCaseHref( lawCase.getCaseHref() );
            if ( lcList.size() > 0 ) { return true; }
        }
            return false;
    }

    private static LawCase PreSaveMossud(LawCase lawCase) {
        if ( lawCase.getCaseSrc() != LawCase.SRC_MOSSUD ) { return lawCase; }

        lawCase.setCaseRegion("77");
        try {
            int ind1 = lawCase.getCaseSide1().indexOf("Истец");
            int ind2 = lawCase.getCaseSide1().indexOf("Ответчик");
            if (ind1 > -1 && ind2 > -1) {
                String side1 = lawCase.getCaseSide1().substring(6, ind2).trim();
                String side2 = lawCase.getCaseSide1().substring(ind2 + 9).trim();
                lawCase.setCaseSide1(side1);
                lawCase.setCaseSide2(side2);
            }
        } catch ( Exception ex ) {
            logger.error(ex.toString());
        }
        return lawCase;
    }
    private static LawCase PreSaveSudrf(LawCase lawCase) {
        if ( lawCase.getCaseSrc() != LawCase.SRC_SUDRF ) { return lawCase; }

        try {
            int ind0 = lawCase.getCaseInfo().indexOf("КАТЕГОРИЯ:");
            int ind1 = lawCase.getCaseInfo().indexOf("ИСТЕЦ(ЗАЯВИТЕЛЬ):");
            int ind2 = lawCase.getCaseInfo().indexOf("ОТВЕТЧИК:");
            if (ind0 > -1 && ind1 > -1 && ind2 > -1) {
                String st = lawCase.getCaseInfo();
                String category = lawCase.getCaseInfo().substring(10, ind1).trim();
                String side1 = lawCase.getCaseInfo().substring(ind1 + 17, ind2).trim();
                String side2 = lawCase.getCaseInfo().substring(ind2 + 9).trim();
                lawCase.setCaseCategory(category);
                lawCase.setCaseSide1(side1);
                lawCase.setCaseSide2(side2);
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
        return lawCase;
    }

}
