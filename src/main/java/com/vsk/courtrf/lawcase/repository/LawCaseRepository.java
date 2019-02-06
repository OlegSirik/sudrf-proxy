package com.vsk.courtrf.lawcase.repository;


import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;


public interface  LawCaseRepository extends CrudRepository<LawCase, Integer> {
    LawCase findById(long id);
    List<LawCase> findByUpdateFlag(String flag);
    List<LawCase> findByDateUpdateAfter(Date date);
    List<LawCase> findByDateInsertAfter(Date date);

    @Query(value = "select * from law_cases where date_trunc('day',date_sync) < current_date or date_sync is null", nativeQuery = true)
    List<LawCase> findCasesForSync();

    @Query(value = "select nextval ('dict_courts_rf_seq')", nativeQuery = true)
    Long getNextId();

    List<LawCase> findByCaseHref(String href);


}


