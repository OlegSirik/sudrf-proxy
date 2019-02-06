package com.vsk.courtrf.court.service;

import com.vsk.courtrf.court.entity.Court;
import com.vsk.courtrf.court.entity.CourtSearchResult;
import com.vsk.courtrf.court.entity.CourtTerritory;
import com.vsk.courtrf.court.entity.Region;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface CourtService {

    List<Court> findAll();
    Court findByCode(String code);

    List<Court> findByRegionIdAndCourtType(int code, String courtType);

    void syncCourt(String code);
    List<Court>  syncCourtRSListByRegion(String region);
    void  syncCourtRSByRegion(String region);
    List<Court>  syncCourtMSListByRegion(String region);
    void syncCourtMSByRegion(String region);
    List<CourtSearchResult> findCourtRSByAddress(String city, String street);
    List<CourtSearchResult> findCourtMSByAddress(String city, String street);

    void syncCourtsList();
    void syncCourtsDetails();

}
