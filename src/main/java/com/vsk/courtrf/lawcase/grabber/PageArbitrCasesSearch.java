package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.util.Dt;
import com.vsk.courtrf.util.Str;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PageArbitrCasesSearch {
    private static final Logger logger = LoggerFactory.getLogger(PageSudrfCasesSearch.class);


    /**
     * @Обработка результатов поиска для арбитражных судов.
     */
    public static void main(String[] args) throws Exception {
        List<LawCase> dt = new ArrayList<LawCase>();
        searchBySubjName("Ренессанс", 30, 30, dt);

    }
    public static String searchBySubjName(String pName, int pDays, int pDelaySec, List<LawCase> dt) {
    //public static String updateAll(String pName, int pDays, int pDelaySec) {

        int pageNr = 0;
        boolean lastPage = true;
        int extraSleep = 0;  // если забанен ip то нужно ждать от 20 мин до 4 часов

        do {
            pageNr ++;

            String bd = "{\"Page\":" + pageNr + ",  \n" +
                    "\"Count\":25,\n" +
                    "\"Courts\":[],\n" +
                    "\"DateFrom\":\"" + Dt.getDaysBefore( pDays ) + "\",\n" +
                    "\"DateTo\":null,\n" +
                    "\"Sides\":[{\"Name\":\"" + pName + "\",\"Type\":-1,\"ExactMatch\":false}],\n" +
                    "\"Judges\":[],\n" +
                    "\"CaseNumbers\":[],\n" +
                    "\"WithVKSInstances\":false}";

            int doCnt = 0;
            boolean doExit = true;

            do {
                try {
                    doCnt ++;
                    doExit = true;

                    Thread.sleep( pDelaySec * 1000 * (doCnt+extraSleep) ); // чтобы не банили
                    lastPage = parsePage( bd, dt);
                    extraSleep = 0;

                } catch (Exception ex) {
                    System.out.println(ex);
                    doExit = false;
                    extraSleep = 20;}
            } while ( !doExit && doCnt < 5 );  // При любых ошибках делается 5 попыток загрузить страницу.
        } while (!lastPage && pageNr < 41 );   // Сайт больше 40 страниц не возвращает

        return "OK";
    }

    public static boolean parsePage( String pBody, List<LawCase> pArray) throws Exception {

        System.out.println(new Date() + "::Arbitr::" + pBody.replace("\n", "") );

        Document doc = Jsoup.connect("http://kad.arbitr.ru/Kad/SearchInstances")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .header( "Host", "kad.arbitr.ru" )
                .header( "Connection","keep-alive")
                .header( "Content-Length", Integer.toString( pBody.length()) )
                .header( "Accept", "*/*")
                .header( "Origin" , "http://kad.arbitr.ru")
                .header( "x-date-format", "iso")
                .header( "X-Requested-With", "XMLHttpRequest")
                .header( "Content-Type", "application/json")
                .header( "Referer", "http://kad.arbitr.ru/")
                .header( "Accept-Encoding", "gzip, deflate")
                .header( "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .requestBody( pBody )
                .post();

//System.out.println(doc);

        String documentsPageSize = doc.select("#documentsPageSize").attr("value");  //25
        String documentsPage = doc.select("#documentsPage").attr("value");  // max 40
        String documentsTotalCount = doc.select("#documentsTotalCount").attr("value"); // real count 83453
        String documentsPagesCount = doc.select("#documentsPagesCount").attr("value");  // real count max 40

        if ( Integer.parseInt(documentsPagesCount) < Integer.parseInt(documentsPage) ) { return true;} //

        for (int curLine=0; curLine < 25; curLine++) {
            LawCase dt = new LawCase();
            //*[@id="b-cases"]/tbody/tr/td[1]/div/div/span
            int div1 = curLine*4;
            int div2 = curLine*4 + 1;
            int div3 = curLine*4 + 2;
            int div4 = curLine*4 + 3;

            String dt1 = doc.select("div.b-container:eq(" + div1 + ")").text();
            String dt2 = doc.select("div.b-container:eq(" + div1 + ") > div").text();
            String dt3 = doc.select("div.b-container:eq(" + div1 + ") > div > span").text();

            dt.setCaseDt   ( Str.stClear( doc.select("div.b-container:eq(" + div1 + ") > div > span").text() ) );
            if ( Str.stNotEmpty(dt.getCaseDt())) {
                dt.setCaseHref ( Str.stClear( doc.select("div.b-container:eq(" + div1 + ") > a").attr("href") ) );
                dt.setCaseNr   ( Str.stClear( doc.select("div.b-container:eq(" + div1 + ") > a").text() ) );

                dt.setCaseJudge( Str.stClear( doc.select("div.b-container:eq(" + div2 + ") > div:eq(0)").text() ) );
                dt.setCaseCourt( Str.stClear( doc.select("div.b-container:eq(" + div2 + ") > div:eq(1)").text() ) );

                String side1_2 = "";

                try {
                    for (int i1=0; i1 < 21; i1++ ) {
                        String side1_3 = Str.stClear( doc.select("div.b-container:eq(" + div3 + ") > div > span").get(i1).childNode(0).outerHtml() ) ;
                        if ( Str.stNotEmpty(side1_2) ) { side1_2 = side1_2 + "::"; }
                        side1_2 = side1_2 + side1_3;
                    }
                } catch (Exception e) {}

                dt.setCaseSide1(side1_2.trim());

                String side2_2 = "";

                try {
                    for (int i1=0; i1 < 21; i1++ ) {
                        String side2_3 = Str.stClear( doc.select("div.b-container:eq(" + div4 + ") > div > span").get(i1).childNode(0).outerHtml() ) ;
                        if ( Str.stNotEmpty(side2_2) ) { side2_2 = side2_2 + "::"; }
                        side2_2 = side2_2 + side2_3;
                    }
                } catch (Exception e) {}

                dt.setCaseSide2(side2_2.trim());

                System.out.println(dt.getCaseNr() + "::" + dt.getCaseSide1() + "::" + dt.getCaseSide2());

                if ( Str.stEmpty( dt.getCaseCourt())) {
                    // судья и название суда находятся в одном div. Если судьи нет, то данные смещаются на 1 элемент.
                    dt.setCaseCourt( dt.getCaseJudge());
                    dt.setCaseJudge("");
                }
                pArray.add(dt);
            }
        }

        return false;
    }

}
