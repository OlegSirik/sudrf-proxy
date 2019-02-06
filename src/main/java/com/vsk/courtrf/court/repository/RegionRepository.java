package com.vsk.courtrf.court.repository;

import com.vsk.courtrf.court.entity.Region;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegionRepository extends CrudRepository<Region, Integer> {
    Region findById(String id);
    Region findByCode (String code);
    List<Region> findAll();
}
