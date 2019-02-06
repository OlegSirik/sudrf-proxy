package com.vsk.courtrf.court.service;

import com.vsk.courtrf.CourtrfApplication;
import com.vsk.courtrf.court.entity.Region;
import com.vsk.courtrf.court.grabber.PageSudrfRegions;
import com.vsk.courtrf.court.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionServiceImpl.class);

    @Autowired
    RegionRepository regionRepo;

    @Override
    public List<Region> findAll() {
        List<Region> regions = new ArrayList<Region>();
        Iterable<Region> itr =  regionRepo.findAll();
        itr.forEach(regions::add);
        return regions;
    }

    @Override
    public Region findByCode(String code) {
        return regionRepo.findByCode(code);
    }

    @Override
    public Region save(Region region) {
        regionRepo.save(region);
        return region;
    }

    @Override
    public void syncRegions() {
        logger.info("Start sync all regions");

        ArrayList<Region> regions = PageSudrfRegions.getRegions();
        regions.forEach(this::save);

        logger.info("Finish sync all regions");
    }
}
