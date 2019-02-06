package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.lawcase.controller.request.AddLawCase;
import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;

import java.util.Date;
import java.util.List;

public interface LawCaseService {
    LawCase findById(Long id);
    List<LawCase> findByUpdateFlag(String flag);
    List<LawCase> findByUpdateDate(Date date);

    /* Добавить условие для ежедневного поиска судебных дел */
    //void searchByWord( LawCaseSearchReq req);
    void addSearchPattern( LawCaseSearchReq req);
    List<LawCaseSearchReq> findAllSearchPatterns();
    void removeSearchPattern( Integer id );

    void syncAllCases(  );
    void syncOneCase( Long id );

    LawCase addLawCase(AddLawCase lawCase);

}
