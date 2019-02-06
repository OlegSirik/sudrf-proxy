package com.vsk.courtrf.lawcase.service;

import com.vsk.courtrf.lawcase.entity.LawCase;

import java.util.List;

public interface LawCaseDB {
    void saveLawCase(LawCase lawCase);
    void saveLawCaseList(List<LawCase> lawCases);
}
