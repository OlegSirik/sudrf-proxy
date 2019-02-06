package com.vsk.courtrf.court.repository;

import com.vsk.courtrf.court.entity.Court;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface  CourtRepository extends CrudRepository<Court, Integer>{
        Court findById(String id);
        Court findByCode(String code);
        List<Court> findByRegionId(int code);
        List<Court> findByRegionIdAndCourtType(int code, String courtType);
        List<Court> findByCourtType(String courtType);

        @Query(value = "select u.* from dict_courts_rf u where u.court_territory_err is null", nativeQuery = true)
        List<Court> findByStatusNull();

        @Query(value = "select u.* from dict_courts_rf u where u.court_territory_err != 'OK'", nativeQuery = true)
        List<Court> findByStatusNotOK();


}
