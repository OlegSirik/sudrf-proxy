package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Court;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Court26MSTerr {


    static List<Court> courts;
    /*
    Поиск тер подсудности по адресу.
    Для ставропольских мировых.
    Нужно передрать в цикле все дома
    */

    static Logger logger = LoggerFactory.getLogger(Court26MSTerr.class );

    private static class tpCourt {
        public String region;
        public String punkt;
        public String street;
        public String house;
        public String court;
        public String courtCode;

        public tpCourt() {
            region = "";
            punkt = "";
            street = "";
            house = "";
            court = "";
            courtCode = "";
        }
    }

    private static List<String> getList1() {
        List<String> step1 = new ArrayList<String>();

        try {
            String url = "http://www.stavmirsud.ru/location/";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .timeout(60*1000)
                    .post();

            Elements nd = doc.select("#region").get(0).children();

            for (Element elmnt: nd) {
                String code = elmnt.attr("value");
                String name = elmnt.text();
                if ( !code.equalsIgnoreCase("") ) {
                    step1.add(code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return step1;
    }

    private static List<String> getList2(String regionCode) {
        List<String> list = new ArrayList<String>();

        try {
            String url = "http://www.stavmirsud.ru/js/showNextMU.php";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .timeout(60*1000)
                    .data("mode", "punkt")
                    .data("val", regionCode)
                    .post();

            Elements nd = doc.select("#punkt_select").get(0).children();

            for (Element elmnt: nd) {
                String code = elmnt.attr("value");
                String name = elmnt.text();
                if ( !code.equalsIgnoreCase("") ) {
                    list.add(code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<String> getList3(String regionCode, String punktCode) {
        List<String> list = new ArrayList<String>();

        try {
            String url = "http://www.stavmirsud.ru/js/showNextMU.php";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .timeout(60*1000)
                    .data("mode", "street")
                    .data("region", regionCode)
                    .data("val", punktCode)
                    .post();

            Elements nd = doc.select("#street_select").get(0).children();

            for (Element elmnt: nd) {
                String code = elmnt.attr("value");
                String name = elmnt.text();
                if ( !code.equalsIgnoreCase("") ) {
                    list.add(code);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void getList4(String regionCode, String punktCode, String streetCode, List<tpCourt> list) {
        try {
            String url = "http://www.stavmirsud.ru/js/showNextMU.php";

            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .timeout(60*1000)
                    .data("mode", "house")
                    .data("region", regionCode)
                    .data("punkt", punktCode)
                    .data("val", streetCode)
                    .post();

            Elements mu = doc.select("#mu_select");
            if ( mu.size() > 0 ) {
                Elements nd = doc.select("#mu_select").get(0).children();

                logger.info("Get courts+houses. URL=" + url + ". region=" + regionCode + ";punktCode=" + punktCode + ";streetCode=" + streetCode);
                logger.info("result = " + nd.size());

                for (Element elmnt : nd) {
                    tpCourt tp = new tpCourt();

                    tp.region = regionCode;
                    tp.punkt = punktCode;
                    tp.street = streetCode;

                    tp.court = elmnt.attr("value");
                    tp.house = elmnt.text();
                    if (!tp.court.equalsIgnoreCase("")) {
                        list.add(tp);
                    }
                }
            } else {
                String str = doc.text();
                int idx1 = str.indexOf("№");
                int idx2 = str.indexOf(" ", idx1);

                tpCourt tp = new tpCourt();

                tp.region = regionCode;
                tp.punkt = punktCode;
                tp.street = streetCode;
                tp.court = str.substring( idx1+1, idx2 );
                tp.house = "";

                logger.info ("EXCEPTION COURT *********************");
                logger.info( regionCode + " " + punktCode + " " + streetCode );
                logger.info(str);
                logger.info ("EXCEPTION COURT *********************");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<tpCourt> getBigList() {
        List<String> list1 = getList1();
        List<tpCourt> list4 = new ArrayList();
        for (String rc1:list1 ) {
            logger.info((rc1));
            List<String> list2 = getList2(rc1);
            for (String rc2 : list2) {
                List<String> list3 = getList3(rc1, rc2);
                for (String rc3 : list3) {
                    getList4(rc1, rc2, rc3, list4);
                }
            }
        }
        return list4;
    }

    private static HashMap getCourt26HashMap(List<String> pCourts26, List<Court> pCourts) {
        HashMap<String, String> retMap = new HashMap();
        boolean found;
        int foundCnt;
        for ( Court court: pCourts ) {
            found = false;
            foundCnt = 0;
            String strNum = court.getName().replaceAll("\\D+", "");
            String regionName = court.getName().substring( court.getName().indexOf(strNum) + strNum.length()).replace("г.","").trim();

            for (String court26: pCourts26) {
                String stRegion10 = regionName.replace("г.", "").substring(0, 5);
                if ( court26.indexOf( stRegion10 ) > -1 ) {
            //        String strNum = court.getName().replaceAll("\\D+", "");
                    retMap.put( court26 + strNum, court.getCode() );
                    found = true;
                    foundCnt ++;
                }
            }
            logger.info ( court.getName() + "found :: " +Integer.toString( foundCnt ));
        }
        return retMap;
    }

    public static void updateTerr(List<Court> pCourts) {
        //courts = pCourts;

        List<String> list1 = getList1();
        HashMap<String,String> court26List = getCourt26HashMap(list1, pCourts);
        HashMap<String,Court>  courtHash = new HashMap();

        for ( Court rc: pCourts ) { courtHash.put( rc.getCode(), rc ); }

        List<tpCourt> list4 = getBigList();

        logger.info("Update territory 26 address list size = " +list4.size());
        logger.info("Court list size = " + pCourts.size());

        int i1 = 0;
        int i2 = 0;
        for (tpCourt court26: list4) {
            //rc.courtCode = getCourtCode(rc);

            i1++;

            String courtCode = court26List.get( court26.region+court26.court );
            court26.courtCode = courtCode;

            if ( courtHash.containsKey( courtCode ) ) {
                Court curCourt = courtHash.get(courtCode);
                curCourt.addTerritory(court26.punkt + " ( " + court26.region + " ) ", court26.street + " " + court26.house);
            } else {
                logger.info( "COURT COE DOES NOT FOUND :: " + court26.region+court26.court );
            }
            if ( i1 == 100 ) {
                i2 = i2 + i1;
                i1 = 0;
                logger.info("rec:" + i2);
            }
        }
        String st = "123";
    }

//    public static void main(String[] args) {
//        System.setProperty("java.net.useSystemProxies", "true");
//        Court26MSTerr ter = new Court26MSTerr();
//        ter.updateTerr();
//    }



}
