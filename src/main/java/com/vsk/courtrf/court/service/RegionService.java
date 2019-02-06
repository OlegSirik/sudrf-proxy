package com.vsk.courtrf.court.service;

import com.vsk.courtrf.court.entity.Region;

import java.util.List;

public interface RegionService {
        List<Region> findAll();
        public Region findByCode(String code);
        public Region save(Region region);
        public void syncRegions();
}
