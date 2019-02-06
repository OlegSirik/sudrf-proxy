package com.vsk.courtrf.court.controller;

import com.vsk.courtrf.court.entity.Court;
import com.vsk.courtrf.court.entity.CourtSearchResult;
import com.vsk.courtrf.court.exception.ExceptionResponse;
import com.vsk.courtrf.court.service.CourtService;
import com.vsk.courtrf.court.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value="court", description="Метода для поиска по справочнику судов и регионов")
public class CourtController {

    private static final Logger logger = LoggerFactory.getLogger(CourtController.class);

    @Autowired
    private RegionService regionService;
    @Autowired
    private CourtService courtService;

    @ApiOperation(value = "Список регионов")
    @RequestMapping(value = "/regions", method= RequestMethod.GET)
    public ResponseEntity getRegions() {
        return ResponseEntity.ok( regionService.findAll() );
    }

    @ApiOperation(value = "Данные по одному региону")
    @RequestMapping(value = "/regions/{code}", method= RequestMethod.GET)
    public ResponseEntity getRegionByCode(@PathVariable("code") String code) {
        return new ResponseEntity<>( regionService.findByCode( code), HttpStatus.OK );
    }

    @ApiOperation(value = "Список районных судов по региону")
    @RequestMapping(value = "/regions/{code}/courts/rs", method= RequestMethod.GET)
    public ResponseEntity getCourtByRegion(@PathVariable("code") int code) {
        return new ResponseEntity<>( courtService.findByRegionIdAndCourtType(code, Court.SUDRF ), HttpStatus.OK  );
    }

    @ApiOperation(value = "Список мировых судов по региону")
    @RequestMapping(value = "/regions/{code}/courts/ms", method= RequestMethod.GET)
    public ResponseEntity getCourtByRegionMir(@PathVariable("code") int code) {
        return new ResponseEntity<>( courtService.findByRegionIdAndCourtType(code, Court.SUDRFMIR ), HttpStatus.OK  );
    }

    @ApiOperation(value = "Данные по одному суду")
    @RequestMapping(value = "/courts/{code}", method= RequestMethod.GET)
    public ResponseEntity getCourtsByCode(@PathVariable("code") String code) {
        return new ResponseEntity<>( courtService.findByCode( code ), HttpStatus.OK );
    }

    @ApiOperation(value = "Поиск районного суда по адресу")
    @RequestMapping(value = "/courts/rs/findByAddress", method= RequestMethod.GET)
    public ResponseEntity findCourtGenByAddress(@RequestParam("city") String city, @RequestParam("street") String street) {

        List<CourtSearchResult> dt = courtService.findCourtRSByAddress(city, street);
        if ( dt.size() > 10) {
            throw new ExceptionResponse("to many results");
        }

        return new ResponseEntity<>( dt, HttpStatus.OK );
    }

    @ApiOperation(value = "Поиск мирового суда по адресу")
    @RequestMapping(value = "/courts/ms/findByAddress", method= RequestMethod.GET)
    public ResponseEntity findCourtMirByAddress(@RequestParam("city") String city, @RequestParam("street") String street) {

        List<CourtSearchResult> dt = courtService.findCourtMSByAddress(city, street);
        if ( dt.size() > 50) {
            throw new ExceptionResponse("to many results");
        }

        return new ResponseEntity<>( dt, HttpStatus.OK );
    }

}
