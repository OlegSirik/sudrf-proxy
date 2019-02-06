package com.vsk.courtrf.lawcase.controller;

import com.vsk.courtrf.court.entity.Court;
import com.vsk.courtrf.lawcase.controller.request.AddLawCase;
import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseSearchReq;
import com.vsk.courtrf.lawcase.service.LawCaseService;

import com.vsk.courtrf.util.Dt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@Api(value="lawcases", description="Метода для поиска по списку отслеживаемых судебных дел")
public class LawCaseController {

    Logger logger = LoggerFactory.getLogger(LawCaseController.class);

    @Autowired
    private LawCaseService lawCaseService;

    /*
    Добавить новое судебное дело для отслеживания
     */
    @PostMapping(path="/lawcases")
    @ApiOperation(value = "Добавление судебного дела для отслеживания")
    public ResponseEntity postNewLawCase(@RequestBody AddLawCase lawCase) {
        LawCase lc = lawCaseService.addLawCase(lawCase);
        return new ResponseEntity<>(  lc, HttpStatus.OK);
        //return "OK";
    }

    /*
    Возвращает список дел, поиск по дате
    */
    @GetMapping(path="/lawcases")
    @ApiOperation(value = "Поиск измененных судебных дел по дате ( dd.mm.yyyy )", produces = "application/xml", consumes = "application/xml")
    @ApiModelProperty(dataType = "java.lang.String", example = "01.01.2019")
    @ResponseBody
    public List<LawCase> findNewCases(
            // ResponseEntity
            //@RequestParam(value="fromDate") @DateTimeFormat(pattern="dd.MM.yyyy") Date fromDate
            @RequestParam(value="fromDate") String fromDate
    ) {
        //return new ResponseEntity<>(
                return lawCaseService.findByUpdateDate( Dt.toDate(fromDate,"dd.MM.yyyy")); //, HttpStatus.OK);
    }

    @GetMapping(path="/lawcases/{id}")
    @ApiOperation(value = "Информация по судебному делу")
    @ResponseBody
    public ResponseEntity findCasesById(
            @RequestParam(value="id") Long id
    ) {
        return new ResponseEntity<>(  lawCaseService.findById(id), HttpStatus.OK);
    }

    /*
    Синхронизация одного дела
    */
    @ApiOperation(value = "Запуск процесса обновления одного судебного дела")
    @PostMapping(path="/lawcases/{id}/sync")
    public String postNewLawCase(@PathVariable("id") Long id) {
        lawCaseService.syncOneCase(id);
        //return new ResponseEntity(  lawCaseService.findById(id), HttpStatus.OK);
        return "Done";
    }

    @ApiOperation(value = "Добавление судебных дел по ключевому слову")
    @PostMapping("admin/lawcases/patterns" )
    public ResponseEntity addSearchPattern(@RequestBody LawCaseSearchReq req) {
        lawCaseService.addSearchPattern( req );
        return ResponseEntity.ok( "OK" );
    }

    @ApiOperation(value = "Список паттернов поиска судебных дел по ключевому слову")
    @GetMapping("admin/lawcases/patterns" )
    public ResponseEntity getSearchPatterns() {
        return ResponseEntity.ok( lawCaseService.findAllSearchPatterns() );

    }

    @ApiOperation(value = "Удалить паттерн поиска судебных дел из списка")
    @DeleteMapping("admin/lawcases/patterns/{id}" )
    public ResponseEntity deleteSearchPatterns( @PathVariable("id") Integer id ) {
        lawCaseService.removeSearchPattern(id);
        return ResponseEntity.ok( "OK" );

    }

}
