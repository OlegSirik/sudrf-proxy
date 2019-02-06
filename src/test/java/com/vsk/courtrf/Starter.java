package com.vsk.courtrf;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.grabber.PageSudrfCasesSearch;
import com.vsk.courtrf.lawcase.grabber.PageSudrfSinglePage;

import java.util.ArrayList;

public class Starter {
    public static void main(String[] args) {
        DBUtil.connectDB();

        ArrayList<LawCase> dt = new ArrayList();
        for (int i=32;i<33;i++) {
            PageSudrfCasesSearch.searchBySubjName( Integer.toString(i), "Ренессанс", 200, 10, dt );
        }

        PageSudrfSinglePage.updateLawCases( 10, dt);
    }

}
