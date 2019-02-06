package com.vsk.courtrf.lawcase.repository;

import com.vsk.courtrf.lawcase.entity.LawCaseCalendar;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface LawCaseCalendarRepo extends CrudRepository<LawCaseCalendar, Integer> {
    List<LawCaseCalendar> findByCaseId(Long id);
    List<LawCaseCalendar> findByCaseIdAndDateUpdateAfter(Long caseId, Date date);

}
