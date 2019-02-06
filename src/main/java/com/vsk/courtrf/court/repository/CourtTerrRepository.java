package com.vsk.courtrf.court.repository;

import com.vsk.courtrf.court.entity.Court;
import com.vsk.courtrf.court.entity.CourtTerritory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface CourtTerrRepository extends CrudRepository<CourtTerritory, Integer> {
    List<CourtTerritory> findByCourtCode(String code);
    List<CourtTerritory> findByCourtCodeAndCol1AndCol2(String code, String col1, String col2);

    @Override
    void deleteAll(Iterable<? extends CourtTerritory> iterable);

    @Query(value = "select u.* from dict_courts_rf_terr u where u.court_code like '%RS%' and u.index_col like %?1% and u.index_col like %?2%", nativeQuery = true)
    List<CourtTerritory> findCourtGenByAddress(String city, String street);

    @Query(value = "select u.* from dict_courts_rf_terr u where u.court_code like '%MS%' and u.index_col like %?1% and u.index_col like %?2%", nativeQuery = true)
    List<CourtTerritory> findCourtMirByAddress(String city, String street);

    @Query(value = "select * from dict_courts_rf_terr where court_code like '%RS%' and to_tsvector(col1)||to_tsvector(col2) @@  plainto_tsquery(?1)", nativeQuery = true)
    List<CourtTerritory> findCourtGenByAddressFull(String cityStreet);

    @Query(value = "select * from dict_courts_rf_terr where court_code like '%MS%' and to_tsvector(col1)||to_tsvector(col2) @@  plainto_tsquery(?1)", nativeQuery = true)
    List<CourtTerritory> findCourtMirByAddressFull(String cityStreet);

}
