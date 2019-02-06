package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Court;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PageSingleCourtMir {

    public static void main(String[] args) throws Exception {

    }


    public static void updateDetail( Court[] pData ) {
        for (Court rc: pData) {
            updateOneSud( rc );
        }
    }

    public static void updateOneSud( Court pData )  {
        try {
            if ( pData.getCode().indexOf("78") == 0 ) {
                parsePagePiter( pData );
            }
            else if ( pData.getCode().indexOf("13") == 0 ) {
                // мордовия
                parsePageMordovia( pData );
            }
            else if ( pData.getCode().indexOf("16") == 0 ) {
                // татария
                parsePageTatar( pData );
            }
            else if ( pData.getCode().indexOf("77") == 0 ) {
                // москва
                parsePageMosSud( pData );
            }
            else if ( pData.getCode().indexOf("24") == 0 ) {
                // Судебные участки мировых судей Красноярского края
                parsePageRegion24( pData ) ;
            }
            else if ( pData.getCode().indexOf("56") == 0 ) {
                // Судебные участки мировых с//
                // пока не работает. страница не загружается
                //parsePageRegion56( pData ) ;
            }
            else if ( pData.getCode().indexOf("71") == 0 ) {
                // Судебные участки мировых с//
                 parsePageRegion71( pData ) ;
            }
            else if ( pData.getCode().indexOf("86") == 0 ) { parsePageRegion86( pData ); }
            else if ( pData.getCode().indexOf("62") == 0 ) { parsePageRegion62( pData ); }
            else if ( pData.getCode().indexOf("26") == 0 ) { parsePageRegion26( pData ); }
            else {
                parsePageRegion62( pData );
                //parsePageJsoup( pData );
            }
            pData.setCourtTerrError("OK");

        } catch (Exception e) {
            System.out.println(e);
            pData.setCourtTerrError( e.toString() );
        }
    }

    public static void parsePageJsoup(Court pData) throws Exception {

        String pSudCode = pData.getCode();
        String pURL = pData.getWww() + "/modules.php?name=terr";

        System.out.println(pURL);
        Document doc = Jsoup
                .connect(pURL)
                //.timeout(60*1000)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

//        Elements nd = doc.select("body > div > div.content").get(0).children();
        Elements nd = doc.select("#tblTerrList > tbody").get(0).children();
        int i = 0;
        for (Element elmnt: nd) {
            if ( i > 1) {
                pData.addTerritory( elmnt.select("div.left").text().trim(), elmnt.select("div.right").text().trim());
            }
            i++;
        }
    }

    public static void  parsePageTatar(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();


        String name = doc.select("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr:nth-child(1) > td.default > span").text();
        if ( pData.getName().isEmpty() || (pData.getName()=="null")) {
            pData.setName(name);
        }
        Elements nd = doc.select("#detail > tbody > tr:nth-child(3) > td > table > tbody").get(0).children();

        String st1 = "Территориальная подсудность:";

        for (Element elmnt: nd) {

            String col1 = elmnt.children().get(0).text().trim();
            String col2 = elmnt.children().get(1).text().trim();

            if ( col1.equalsIgnoreCase( st1 )) {
                pData.addTerritory( col1, col2 );
            }
            if ( col1.equalsIgnoreCase("Адрес:")) {
                pData.setAddress( col2 );
            }
            if ( col1.equalsIgnoreCase("E-mail:")) {
                pData.setEmail( col2 );
            }
            if ( col1.equalsIgnoreCase("Телефон для справок:")) {
                pData.setPhone( col2 );
            }
        }

    }

    public static int parsePagePiter(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        pData.setAddress( doc.select("body > div.inner-wrapper > main > section.sector-info > div > div > div:nth-child(1) > div > div:nth-child(1) > div > div > p" ).text() );
        pData.setPhone( doc.select("body > div.inner-wrapper > main > section.sector-info > div > div > div:nth-child(1) > div > div:nth-child(2) > div.col-lg-7 > div > div:nth-child(1) > div > p").text().trim() );
        pData.setEmail( doc.select( "body > div.inner-wrapper > main > section.sector-info > div > div > div:nth-child(1) > div > div:nth-child(3) > div > div > a" ).text().trim() );

        Elements nd = doc.select("body > div.inner-wrapper > main > section.full-sector-info > div > div > div:nth-child(2) > article.territorial > div > div > table > tbody").get(0).children();

        //String st1 = "Территориальная подсудность:";

        int rn = 1;
        for (Element elmnt: nd) {

            if ( rn > 1 ) {

                String col1 = elmnt.children().get(0).text().trim();
                String col2 = elmnt.children().get(1).text().trim();
                String col3 = elmnt.children().get(2).text().trim();

                if ( !col2.isEmpty() ) {
                    pData.addTerritory( col1, col2);
                }
                if ( !col3.isEmpty() ) {
                    pData.addTerritory( col1, col3);
                }

            }
            rn = rn + 1;
        }
        return 0;
    }

    public static int parsePageMordovia(Court pData) throws Exception {

        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        String mirNr = pSudCode.substring(6,8);  //"13MS0041"

        System.out.println(pURL);
        Document doc = Jsoup.connect("http://mirsud.e-mordovia.ru/api/Territories")
                .timeout(1000*60)
                .header("Referer", "http://mirsud.e-mordovia.ru/Home/Territories/" + mirNr)
                //.header( "Content-Type", "application/xml")
                .header( "Content-Type", "text/*")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();


        Elements nd1 = doc.select("body");

        Elements nd = doc.select("body").get(0).children();

        //String st1 = "Территориальная подсудность:";

        ArrayList<String> rows = new ArrayList<String>(nd.size());
        for (Element elmnt: nd) {
            rows.add(elmnt.text().trim());
        }

        List<String> s1 = Arrays.asList("01");
        List<String> s2 = Arrays.asList( "02","04","05","06","07","08","09","10","12","13","14","16","22","23","25","32","34","35","36","37","38","39","40","41","44");
        List<String> s3 = Arrays.asList( "15","18","19","20","21" ); // Саранск
// 03, 11,
// 26,27,28,29,30,31, 42,43

        if (  s1.contains( mirNr )) {
            modrovType1(pData, rows);
        }
        if ( s2.contains( mirNr ) ) {
            modrovType2(pData, rows,"");
        }
        if ( s3.contains( mirNr ) ) {
            modrovType3(pData, "г. Саранск", rows);
        }
        if ( mirNr.equalsIgnoreCase("33") ) {
            modrovType2(pData, rows,"г. Рузаевка");
        }

        return 0;
    }

    public static void modrovType1(Court pData, ArrayList<String> pRows) {
        // одна строка через ;
        String str = pRows.get(1);
        String[] strs = str.split(";");

        for (String st: strs) {
            pData.addTerritory( st, "");
        }
    }

    public static boolean isStreet(String pStr) {
        List<String> st_street = Arrays.asList("ул.", "пер.", "проезд" );
        for (String st: st_street) {
            if ( pStr.toLowerCase().startsWith(st)) {
                return true;
            }
        }
        return false;
    }

    public static void modrovType2(Court pData, ArrayList<String> pRows, String pCity) {
        // коллекция с улицами и без;

        List<String> st_ex = Arrays.asList("Территориальная подсудность", "Населенный пункт", "Адрес");
        String col1 = "";
        String cityName = "City";

        boolean insert = true;

        for (int i=0; i<pRows.size(); i++) {

            String st = pRows.get(i).replace("<p>", "").replace("</p>", "").trim();
            if (!st_ex.contains(st)) {

                if ( isStreet(st) ) {
                    pData.addTerritory( cityName, st);
                } else {
                    cityName = st;
                    if ( i == pRows.size()-1 ) {
                        // последняя строка. добавляем
                        pData.addTerritory( cityName, "");
                    } else {
                        String nextSt = pRows.get(i+1).replace("<p>", "").replace("</p>", "").trim();
                        // если следующая строка это город, то добавлем текущий город
                        if ( !isStreet(nextSt) ) {
                            pData.addTerritory( cityName, st);
                        }
                    }
                }
            }
        }
    }

    public static void modrovType3(Court pData, String pCity, ArrayList<String> pRows) {
        // только улицы определенного города ;
        for (String str: pRows) {
            String st = str.replace("<p>", "").replace("</p>", "").trim();

            pData.addTerritory( pCity, st);
        }
    }

    public static void parsePageMosSud(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        HashMap<String, String> dt = new HashMap();

        Element tbl = doc.select ("#detail > tbody > tr:nth-child(3) > td > table > tbody").get(0);

        for (Element rc: tbl.children()) {
            Element el = (Element)rc;
            String s1 = ((Element)el.childNodes().get(0)).text().trim();
            String s2 = ((Element)el.childNodes().get(1)).text().trim();
            dt.put(s1, s2);
        }

        pData.setAddress( dt.getOrDefault("Адрес:", "") );
        pData.setPhone( dt.getOrDefault("Телефон судебного участка:", "") );
        pData.setEmail( dt.getOrDefault("E-mail:", ""));
        pData.addTerritory( "Москва", dt.getOrDefault("Территориальная подсудность:", "") );

        pData.setCourtDistrict( dt.getOrDefault("Судебный район:", ""));
        pData.setParentCourt( dt.getOrDefault("Вышестоящий суд:", ""));
        pData.setCityDistrict( dt.getOrDefault("Муниципальный район:", ""));

    }

    public static void parsePageRegion24(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();


        pData.setAddress( doc.select( "#mainContent > table:nth-child(20) > tbody > tr > td:nth-child(3)" ).text().replace("посмотреть на карте города »","").trim() );
        pData.setPhone( doc.select( "#mainContent > table:nth-child(16) > tbody > tr > td:nth-child(3)" ).text().trim());
        pData.setEmail( doc.select( "#mainContent > table:nth-child(18) > tbody > tr > td:nth-child(3) > a" ).attr("href").trim());

        String adr = doc.select( "#dog" ).text().trim();
        if ( adr.isEmpty() ) {
            adr = doc.select( "#mainContent > table:nth-child(24) > tbody > tr:nth-child(2) > td" ).text().trim();
        }
        pData.addTerritory( "", adr );

        pData.setParentCourt( doc.select( "#mainContent > table:nth-child(26) > tbody > tr > td:nth-child(3)" ).text().trim());


    }

    public static void parsePageRegion56(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww() + "/jurisdiction";

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();


        Elements ul = doc.select( "#home-latest-posts > article > div:nth-child(1) > div:nth-child(2) > ul" );
        for (Element el: ul ) {
            pData.addTerritory( "", el.text() );
        }

        //pData.setParentCourt( doc.select( "#mainContent > table:nth-child(26) > tbody > tr > td:nth-child(3)" ).text().trim());
    }

    public static void parsePageRegion71(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = ( pData.getWww() + "podsud/" );

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();


        Elements td = doc.select( "body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td" );
        Elements p = td.select("p");
        String pText = "";
        if ( p.size() != 0 ) {
            Elements inTable = p.select("table > tbody > tr");
            if ( inTable.size() > 0 ) {
                   int i = 0;
                   for ( Element tr:inTable) {
                       i++;
                       String city = "";
                       if ( i > 2 ) {
                           String col1 = tr.select("td").get(0).text();
                           if ( !col1.isEmpty() ) { city = col1; }
                           String col2 = tr.select("td").get(1).text();
                           String col3 = tr.select("td").get(2).text();
                           pData.addTerritory( city, col2 + " " + col3);
                       }
                   }
            } else {
                pText = p.text();
            }
        }
        Elements ul = td.select("ul");
        if ( ul.size() != 0 ) {
            pText = pText+  " " + ul.text() ;
        }
        pData.addTerritory( "", pText );

        Elements divs = td.select( "div.terr-item" );
        if ( divs.size() != 0 ) {
            for (Element tr:divs) {
                String left = tr.select("div.left").get(0).text().trim();
                String right = tr.select("div.right").get(0).text().trim();
                if ( ! ( right.equalsIgnoreCase("Адрес") || right.trim().isEmpty() )) {
                    pData.addTerritory(left, right);
                }
            }
        }


        //pData.setParentCourt( doc.select( "#mainContent > table:nth-child(26) > tbody > tr > td:nth-child(3)" ).text().trim());
    }

    public static void parsePageRegion86(Court pData) throws Exception {
        String pSudCode = pData.getCode();
        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        HashMap<String, String> dt = new HashMap();

        Element tbl = doc.select ("#detail > tbody > tr:nth-child(3) > td > table > tbody").get(0);

        for (Element rc: tbl.children()) {
            Element el = (Element)rc;
            String s1 = ((Element)el.childNodes().get(0)).text().trim();
            String s2 = ((Element)el.childNodes().get(1)).text().trim();
            dt.put(s1, s2);
        }

        pData.setAddress( dt.getOrDefault("Адрес судебного участка:", "") );
        pData.setPhone( dt.getOrDefault("Телефон:", "") );
        pData.setEmail( dt.getOrDefault("E-mail:", ""));
        pData.addTerritory( "", dt.getOrDefault("Территориальная подсудность:", "") );

   //     pData.setCourtDistrict( dt.getOrDefault("Судебный район:", ""));
   //     pData.setParentCourt( dt.getOrDefault("Вышестоящий суд:", ""));
   //     pData.setCityDistrict( dt.getOrDefault("Муниципальный район:", ""));

    }

    public static void parsePageRegion26(Court pData) throws Exception {

    }

    public static void parsePageRegion62(Court pData) throws Exception {

        String pURL = pData.getWww();

        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        pData.setAddress( doc.select("#court_address").text().trim() );
        //pData.setPhone( dt.getOrDefault("Телефон:", "") );
        pData.setEmail( doc.select("#court_email > a").attr("href").trim());


        //     pData.setCourtDistrict( dt.getOrDefault("Судебный район:", ""));
        //     pData.setParentCourt( dt.getOrDefault("Вышестоящий суд:", ""));
        //     pData.setCityDistrict( dt.getOrDefault("Муниципальный район:", ""));


        /***** 2 ******/
        pURL = pURL + "/modules.php?name=terr";

        System.out.println(pURL);
        doc = Jsoup.connect(pURL)
                .timeout(1000*60)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        Elements tbl = doc.select ("div.terr-item");

        int i = 0;
        for (Element rc: tbl) {
            i++;
            Element el = (Element) rc;
            String s1 = ((Element) el.childNodes().get(1)).text().trim();
            String s2 = ((Element) el.childNodes().get(3)).text().trim();
            if (i > 1) {
                pData.addTerritory(s1, s2);
            }
        }
    }

}
