package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.util.Dt;
import com.vsk.courtrf.util.Str;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PageMosSudCasesSearch {
    private static final Logger logger = LoggerFactory.getLogger(PageSudrfCasesSearch.class);

    public static void main(String[] args) throws Exception {

        ArrayList<LawCase> dt = new ArrayList<LawCase>();
        searchBySubjName("Ренессанс", 20, 2, dt);

    }

    public static String searchBySubjName(String pName, int pDays, int pDelaySec, List<LawCase> dt) {

        logger.info( "PageMosSudCasesSearch.searchBySubjName ::"+pName);

        int pageNr = 0;
        boolean lastPage = true;

        do {

            pageNr++;

            String url = "http://www.mos-gorsud.ru/search?"
//                    + "courtAlias=&caseNumber=&"
//                    + "instance=&"
//                    + "processType=&"
//                    + "codex=&"
//                    + "judge=&"
//                    + "publishingState=&"
//                    + "documentType=&"
                    + "participant=" + pName + "&"
//                    + "year=&"
                    + "caseDateFrom=" + Dt.getDaysBefore( pDays ) + "&"
//                    + "caseDateTo=&"
//                    + "caseFinalDateFrom=&"
//                    + "caseFinalDateTo=&"
//                    + "caseLegalForceDateFrom=&"
//                    + "caseLegalForceDateTo=&"
//                    + "docsDateFrom=&"
//                    + "docsDateTo=&"
//                    + "documentStatus=&"
                    + "page=" + pageNr;

            int doCnt = 0;
            boolean doExit = true;

            do {
                try {
                    doCnt ++;
                    doExit = true;

                    Thread.sleep( pDelaySec * 1000 * doCnt); // чтобы не банили
                    lastPage = parsePage(url, dt);

//                    dt.clear();
                } catch (Exception ex) {
                    System.out.println(ex);
                    doExit = false;
                }
            } while ( !doExit && doCnt < 5 );
        } while (!lastPage && pageNr < 1001 );

        return "OK";
    }

    public static boolean parsePage(String pURL, List<LawCase> pArray) throws Exception {

        int maxPage = 1;
        int curPage = 1;

        System.out.println("MosSud::"+pURL);

        Document doc = Jsoup.connect( pURL )
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .timeout( 30 * 1000)
                .get();

        String resultSearch = doc.select("#content > div.resultsearch_text").text();
        if ( resultSearch.equalsIgnoreCase("По вашему запросу ничего не найдено") ) {
            System.out.println("NOT FOUND");
            return true;
        }

        try {
            maxPage = Integer.parseInt( doc.select("#paginationFormMaxPages").attr("value") );
            curPage = Integer.parseInt( doc.select("#paginationFormInput").attr("value") );
        } catch (Exception e) {}

        System.out.println("MosSud::Page" + curPage +"::FROM::" +maxPage);

        for (int i=1;i<20;i++) {
            LawCase dt = new LawCase();

            dt.setCaseNr(doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(1) > nobr > a").html());
            dt.setCaseHref( "https://www.mos-gorsud.ru" + doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(1) > nobr > a").attr("href"));
            dt.setCaseSide1(doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(2) > div > div.right").text());
            //3
            dt.setCaseStatus( doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(3) > div > div").text() );
            // 4
            dt.setCaseJudge( doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(4) > div > div").text() );
            // 5
            //dt.setCaseCode( doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(5) > div > div").text() );
            // 6
            dt.setCaseCategory( doc.select("#content > div.wrapper-search-tables > table > tbody > tr:nth-child("+i+") > td:nth-child(6) > div > div").text() );

            if ( Str.stNotEmpty( dt.getCaseNr() ) ) { pArray.add(dt); }
            //System.out.println(caseNr + "::" + caseSides +"::"+caseStatus+"::"+caseJudge+"::"+caseCode+"::"+ caseCategory  );
        }

        if ( curPage >= maxPage ) {return true; }

        return false;
    }

}
