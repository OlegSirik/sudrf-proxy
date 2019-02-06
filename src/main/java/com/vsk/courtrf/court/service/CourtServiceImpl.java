package com.vsk.courtrf.court.service;

import com.vsk.courtrf.court.entity.Court;
import com.vsk.courtrf.court.entity.CourtSearchResult;
import com.vsk.courtrf.court.entity.CourtTerritory;
import com.vsk.courtrf.court.entity.Region;
import com.vsk.courtrf.court.exception.ExceptionResponse;
import com.vsk.courtrf.court.grabber.*;
import com.vsk.courtrf.court.grabber.court77rs.SrcResult;
import com.vsk.courtrf.court.repository.CourtRepository;

import com.vsk.courtrf.court.repository.CourtTerrRepository;
import com.vsk.courtrf.court.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CourtServiceImpl implements CourtService {

    private static final Logger logger = LoggerFactory.getLogger(CourtServiceImpl.class);

    @Autowired
    private CourtRepository courtRepo;
    @Autowired
    private CourtTerrRepository courtTerrRepo;
    @Autowired
    private RegionRepository regionRepo;

    @Override
    public List<Court> findAll() {
        return null;
    }

    @Override
    public Court findByCode(String code) {
        Court court = courtRepo.findByCode( code );
        List<CourtTerritory> courtTerr = courtTerrRepo.findByCourtCode(code);
        court.setCourt_territory( courtTerr );
        return court;
    }


    public List<Court> findByStatusNull() {
        return courtRepo.findByStatusNull();
    }

    public List<Court> findByStatusNotOK() {
        return courtRepo.findByStatusNotOK();
    }

    public List<Court> findByRegionId(int code) {
        List<Court> courts = courtRepo.findByRegionId( code );
        return courts;
    }
    @Override
    public List<Court> findByRegionIdAndCourtType(int code, String courtType) {
        List<Court> courts = courtRepo.findByRegionIdAndCourtType( code, courtType );
        return courts;
    }

    @Override
    public void syncCourt(String code) {
        logger.info( "START Sync court :: " + code);

        Court court = courtRepo.findByCode( code );
        List<CourtTerritory> courtTerr = courtTerrRepo.findByCourtCode( code );

        if ( court.getCourtType().equalsIgnoreCase( Court.SUDRF ) ) {
            PageSingleCourt.updateOneSud( court );

            saveCourt( court );
            courtTerrRepo.deleteAll(courtTerr);
            courtTerrRepo.saveAll(court.getCourt_territory());
        } else {
            PageSingleCourtMir.updateOneSud( court );

            saveCourt( court );
            courtTerrRepo.deleteAll(courtTerr);
            courtTerrRepo.saveAll(court.getCourt_territory());
        }
    }

    public void syncAllCourt() {
        Iterable<Court> courts = courtRepo.findByStatusNull(); // .findAll();
        courts.forEach(item->syncCourt(item.getCode()));
    }


    /*
    Загрузка списка судов по региону
     */
    @Override
    public List<Court>  syncCourtRSListByRegion(String region) {
        List<Court> courts = PageSudrfCourts.getByRegion( region );
        for ( Court rc: courts ) {
            saveCourt( rc );
        }
        return courts;
    }

    @Override
    public List<Court>  syncCourtMSListByRegion(String region) {
        ArrayList<Court> courts = PageSudrfCourtsMir.getByRegion( region );
        for ( Court rc: courts ) {
            saveCourt( rc );
        }
        return courts;
    }


    /*
    Загрузка по каждому суду
     */
    @Override
    public void syncCourtMSByRegion(String region) {
        List<Court> courts = courtRepo.findByRegionIdAndCourtType(Integer.parseInt(region), "MS");

        if ( region.equalsIgnoreCase("26")) {
            /* stavropol */
            Court26MSTerr.updateTerr( courts );

            for ( Court rc:courts) {
                List<CourtTerritory> courtTerr = courtTerrRepo.findByCourtCode( rc.getCode() );
                courtTerrRepo.deleteAll(courtTerr);
                courtTerrRepo.saveAll( rc.getCourt_territory() );
            }
        }

        for ( Court rc: courts ) {
            syncCourt( rc.getCode() );
        }
    }

    @Override
    public void syncCourtRSByRegion(String region) {
        List<Court> courts = courtRepo.findByRegionIdAndCourtType(Integer.parseInt(region), "RS");
        for ( Court rc: courts ) {
            syncCourt( rc.getCode() );
        }
    }

    private List<CourtSearchResult> convert2Court ( List<CourtTerritory> dt ) {

        HashMap<String, CourtSearchResult> courts = new HashMap();

        for (CourtTerritory rc: dt) {
            if ( !courts.containsKey( rc.getCourtCode() ) ) {
                Court crt = courtRepo.findByCode( rc.getCourtCode() );
                CourtSearchResult rslt = new CourtSearchResult ( crt.getId(), crt.getCode(), crt.getAddress(), crt.getPhone(), crt.getEmail(), crt.getWww(), crt.getName() );
                courts.put( crt.getCode(), rslt );
            }

            CourtSearchResult rslt = courts.get( rc.getCourtCode() );
            rslt.addAddress( rc.getIndexCol());

        }
        return new ArrayList( courts.values() );
    }

    private void syncCourtTerrRS77ByAddress(String street) {
    /* загрузка подсудности по адресу для москвы*/
        String[] streets = Court77RSTerr.getStreets4Street( street );
        for (int i=0; i < streets.length  ; i++) {
            List<SrcResult> courts = Court77RSTerr.getCourttCodes( streets[i]);
            for (SrcResult rc: courts ) {
                List<CourtTerritory> crtTerrs = courtTerrRepo.findByCourtCodeAndCol1AndCol2( rc.getCourt(), "г. Москва", streets[i]+" "+rc.getHouse());
                if ( crtTerrs.size() == 0 ) {
                    CourtTerritory newCourtTerr = new CourtTerritory(rc.getCourt(), "г. Москва", streets[i] + " " + rc.getHouse());
                    courtTerrRepo.save(newCourtTerr);
                }
            }
        }
    }
    @Override
    public List<CourtSearchResult> findCourtRSByAddress(String city, String street) {
        String lcity = " " + city.toLowerCase().trim() + " ";
        String lstreet = " " + street.toLowerCase().trim() + " ";
        List<CourtTerritory> dt = courtTerrRepo.findCourtGenByAddress( lcity, "");
        if ( dt.size() == 1 ) {
            return convert2Court( dt );
        }

        dt = courtTerrRepo.findCourtGenByAddressFull(lcity+" "+lstreet);
        if ( dt.size() == 0 && city.toLowerCase().indexOf("москва") > -1 ) {
            syncCourtTerrRS77ByAddress(street);
            dt = courtTerrRepo.findCourtGenByAddressFull(lcity+" "+lstreet);
        }

        return convert2Court( dt );
    }

    @Override
    public List<CourtSearchResult> findCourtMSByAddress(String city, String street) {
        String lcity = city.toLowerCase().trim();
        String lstreet = street.toLowerCase().trim();
        List<CourtTerritory> dt = courtTerrRepo.findCourtMirByAddress( lcity, "");
        if ( dt.size() == 1 ) {
            return convert2Court( dt );
        }

        dt = courtTerrRepo.findCourtMirByAddressFull(lcity+" "+lstreet);
        return convert2Court( dt );
    }

    private void saveCourt(Court court) {
        court.setCourtType( court.getCode().substring(2,4) );
        court.setRegionId( Integer.parseInt(court.getCode().substring(0,2)));

        Court courtDB = courtRepo.findByCode( court.getCode() );
        if ( courtDB == null ) {
            court.setLastUpdate( new Date());
            courtRepo.save( court );
        } else {
            String str1 = court.toString();
            String str2 = courtDB.toString();
            if ( !court.toString().equalsIgnoreCase(courtDB.toString()) ) {
                court.setId( courtDB.getId() );
                court.setLastUpdate( new Date());
                courtRepo.save( court );
            }
        }

    }


    public void syncCourtsList() {
        List<Region> regions = regionRepo.findAll();
        for (Region rc:regions) {
            syncCourtRSListByRegion( rc.getCode() );
            syncCourtMSListByRegion( rc.getCode() );
        }

    }

    public void syncCourtsDetails() {
        List<Region> regions = regionRepo.findAll();
        for (Region rc:regions) {
            syncCourtRSByRegion( rc.getCode() );
            syncCourtMSByRegion( rc.getCode() );
        }
    }

}
