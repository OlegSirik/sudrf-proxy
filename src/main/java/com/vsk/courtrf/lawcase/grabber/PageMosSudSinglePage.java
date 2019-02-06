package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.util.Str;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class PageMosSudSinglePage {

    public static void main(String[] args) {
        LawCase dt = new LawCase();
        dt.setCaseHref("http://www.mos-gorsud.ru/rs/babushkinskij/services/cases/civil/details/3f5abfae-638d-4de0-adaa-62fa980115b2?");
        parsePage( dt);
    }

    public static void updateLawCase(int pDelaySec, LawCase lawCase) {
        //System.out.println(new Date() + "::MosSud Page row:"+curRow);
        //int curRow = 1;
        //curRow++;
        int doCnt = 0;
        boolean doExit = true;

        do {
            try {
                doCnt ++;
                doExit = true;

                parsePage( lawCase );
                Thread.sleep( pDelaySec * 1000);  // чтобы не забанили
                lawCase.setDateSync( new Date());

            } catch (Exception ex) {
                System.out.println(ex);
                doExit = false;
            }
        } while ( !doExit && doCnt < 5 );
    }

    public static void updateLawCases(int pDelaySec, ArrayList<LawCase> pCases) {
        for (LawCase rc: pCases) {
            updateLawCase( pDelaySec, rc );
        }
    }

    public static void parsePage(LawCase pCase) {
        try {

            Document doc = Jsoup.connect( pCase.getCaseHref().replace("https","http"))
                    .timeout(1000*60)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .get();

            String court_name = doc.select("ul.breadcrumb > li:nth-child(2)").text();
            //pArray.add(new grssudrf.data.SudrfSinglePage(0, 0, "СУД", court_name,"","","",""));

            HashMap<String, String> dt = new HashMap();

            int rowNr = 1;

            for (int rn=1; rn<30; rn++) {

                String col1 = doc.select("#content > div > div.detail-cart > div > div:nth-child(" + rn + ") > div.left").text();
                String col2 = doc.select("#content > div > div.detail-cart > div > div:nth-child(" + rn + ") > div.right").text();

                if ( Str.stNotEmpty(col1) && Str.stNotEmpty(col2)) {
                    //pArray.add(new grssudrf.data.SudrfSinglePage(0, rn, col1,col2,"","","",""));
                    pCase.addDetail( 0, rn, col1,col2,"","","","" );
                    dt.put(col1, col2);
                    rowNr ++;
                } else {
                    if ( rowNr > 1 ) { break;}
                }

            }

            pCase.setCaseCode ( dt.getOrDefault("Уникальный идентификатор дела", pCase.getCaseCode()) );
            pCase.setCaseNr( dt.getOrDefault("Номер дела", pCase.getCaseNr()) );
            pCase.setCaseSide1( dt.getOrDefault("Стороны", pCase.getCaseSide1()) );
            pCase.setCaseDt( dt.getOrDefault("Дата регистрации", pCase.getCaseDt()) );
            pCase.setCaseJudge( dt.getOrDefault( "Cудья", pCase.getCaseJudge()));
            pCase.setCaseCategory( dt.getOrDefault( "Категория дела", pCase.getCaseCategory()));
            pCase.setCaseStatus( dt.getOrDefault( "Текущее состояние", pCase.getCaseStatus()));

            try {
                String col1 = doc.select("body > div.wrapper > main > section > div.mainblock_title > ul > li:nth-child(2) > a").text().trim();
                pCase.setCaseCourt( col1 );
            } catch (Exception e) {}

            for (int rn=1; rn<30; rn++) {

                String col1 = Str.stClear( doc.select("#tabs-1 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(1)").text() );
                String col2 = Str.stClear( doc.select("#tabs-1 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(2)").text() );
                String col3 = Str.stClear( doc.select("#tabs-1 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(3)").text() );

                if ( Str.stNotEmpty(col1)) {
                    pCase.addDetail(1, rn, col1, col2, col3, "", "", "");
                }
            }

            String col0 = Str.stClear( doc.select("#tabs-2 > div > table > thead > tr > th:nth-child(1)").text() );
            if (col0.equalsIgnoreCase("Дата и время")) {
                for (int rn=1; rn<30; rn++) {

                    String col1 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(3)").text() );

                    String col2 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(1)").text() );

                    if ( Str.stEmpty(col2)) { break; }
                    //String col2 = Utl.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(1)").text() );
                    String col3 = col2.substring(11);
                    col2 = col2.substring(0,10);
                    //String col3 = Utl.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(3)").text() );
                    String col4 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(4)").text() );
                    String col5 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(5)").text() );



                    if ( Str.stNotEmpty(col1)) {
                        //pArray.add(new grssudrf.data.SudrfSinglePage(2, rn, col1, col2, col3, col4, col5, ""));
                        pCase.addDetail( 2, rn, col1, col2, col3, col4, col5, "");
                    }
                }
            } else {
                for (int rn=1; rn<30; rn++) {

                    String col1 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(4)").text() );
                    String col2 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(1)").text() );
                    String col3 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(2)").text() );
                    String col4 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(5)").text() );
                    String col5 = Str.stClear( doc.select("#tabs-2 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(6)").text() );

                    if ( Str.stNotEmpty(col1)) {
                        pCase.addDetail( 2, rn, col1, col2, col3, col4, col5, "");
                    }
                }
            }

            for (int rn=1; rn<30; rn++) {

                String col1 = Str.stClear( doc.select("#tabs-3 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(1)").text() );
                String col2 = Str.stClear( doc.select("#tabs-3 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(2)").text() );
                String col3 = Str.stClear( doc.select("#tabs-3 > div > table > tbody > tr:nth-child("+rn+") > td:nth-child(3)").text() );

                if ( Str.stNotEmpty(col1)) {
                    pCase.addDetail(3, rn, col1, col2, col3, "", "", "");
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
