package com.vsk.courtrf.lawcase.repository;

import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LawCaseSearchRepo extends CrudRepository<LawCaseSearchReq, Integer> {
}
