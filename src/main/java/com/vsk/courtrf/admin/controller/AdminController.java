package com.vsk.courtrf.admin.controller;

import com.vsk.courtrf.court.service.CourtService;
import com.vsk.courtrf.court.service.RegionService;
import com.vsk.courtrf.lawcase.service.LawCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="admin", description="Метода для обновления справочников")
public class AdminController {

    @Autowired
    CourtService courtService;
    @Autowired
    RegionService regionService;
    @Autowired
    LawCaseService lcService;


    @ApiOperation(value = "Запустить процесс загрузки справочника регионов")
    @PostMapping(value = "/admin/regions/sync")
    public ResponseEntity syncRegions() {
        regionService.syncRegions();
        return ResponseEntity.ok( "Started" );
    }

    @ApiOperation(value = "Загрузка справочника судов")
    @PostMapping(value = "/admin/courts/synclist")
    public ResponseEntity syncAllCourtList() {
        courtService.syncCourtsList();
        return ResponseEntity.ok( "OK" );
    }

    @ApiOperation(value = "Загрузка тер.подсудности по всему справочника судов")
    @PostMapping(value = "/admin/courts/syncdetail")
    public ResponseEntity syncAllCourtDetails() {
        courtService.syncCourtsDetails();
        return ResponseEntity.ok( "OK" );
    }

    @ApiOperation(value = "Загрузка справочника мировых судов для региона")
    @PostMapping(value = "/admin/regions/{code}/courts/ms/sync")
    public ResponseEntity syncCourtsMirByRegion(@PathVariable("code") String code) {
        courtService.syncCourtMSByRegion( code );
        return ResponseEntity.ok( "OK" );
    }

    @ApiOperation(value = "Загрузка справочника районных судов для региона")
    @PostMapping(value = "/admin/regions/{code}/courts/rs/sync")
    public ResponseEntity syncCourtsGenByRegion(@PathVariable("code") String code) {
        courtService.syncCourtRSByRegion( code );
        return ResponseEntity.ok( "OK" );
    }

    @ApiOperation(value = "Загрузка одного суда")
    @PostMapping(value = "/admin/courts/{code}/sync")
    public ResponseEntity syncCourtsByCode(@PathVariable("code") String code) {
        courtService.syncCourt( code );
        return ResponseEntity.ok( courtService.findByCode(code) );
    }



}
