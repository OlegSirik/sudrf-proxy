package com.vsk.courtrf.lawcase.repository;

import com.vsk.courtrf.lawcase.entity.LawCaseDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LawCaseDetailsRepository extends CrudRepository<LawCaseDetails, Integer> {
    List<LawCaseDetails> findByCaseId(Long id);
    List<LawCaseDetails> findByCaseIdAndPageNr(Long id, int pageNr);
}
