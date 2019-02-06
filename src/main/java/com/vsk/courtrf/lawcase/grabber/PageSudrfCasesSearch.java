package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.util.Str;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageSudrfCasesSearch {

    private static final Logger logger = LoggerFactory.getLogger(PageSudrfCasesSearch.class);

    static String arr[] = { "23", "32","66" };
    static Set setPageType1 = new HashSet(Arrays.asList(arr));

    public static void main(String[] args) {
        ArrayList<LawCase> dt = new ArrayList();
        for (int i=32;i<99;i++) {
            searchBySubjName( Integer.toString(i), "Ренессанс", 200, 10, dt );
        }
    }

    public static void searchBySubjName(String pRegionId, String pName, int pDays, int pDelaySec, List<LawCase> dt ) {

        int pageCnt = 0;
        boolean lastPage = true;

        do
        {
            pageCnt ++;

            int doCnt = 0;
            boolean doExit = true;

            do {
                try {
                    doCnt ++;
                    doExit = true;

                    String pageURL = "http://sudrf.ru/index.php?id=300&page=" + pageCnt
                            + "&act=go_sp_search&searchtype=sp&court_subj=" + pRegionId
                            + "&suds_subj="
                            + "&num_d="
                            + "&f_name=" + pName
                            + "&date_num_in=" + getDaysBefore( pDays )
                            + "&date_num_out="
                            + "&suds_vid="
                            + "&spkatg="
                            + "&suds_pip="
                            + "&st_cat="
                            + "&sud_pip="
                            ;

                    Thread.sleep( pDelaySec * 1000 * doCnt ); // чтобы не банили. Ждем 20 секунд
                    logger.info("searchBySubjName::GET::Attempt::" + doCnt + "::URL::" + pageURL );
                    lastPage = parsePageJsoup(pageURL, dt, pRegionId);
//                    lastPage=true;
                } catch (Exception ex) {
                    System.out.println(ex);
                    logger.error( ex.toString() );
                    doExit = false;    }
            } while ( !doExit && doCnt < 5 );
        } while (!lastPage && pageCnt < 301 );
    }

    private static String getDaysBefore(int pDays) {

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1 * pDays);

        return String.format("%02d", c.get(Calendar.DATE)) + "."
                + String.format("%02d", ( c.get(Calendar.MONTH) + 1) )
                + "." + c.get(Calendar.YEAR);
    }

    private static boolean parsePageJsoup(String pURL, List pArray, String pRegionId) throws Exception {
        // Если вылетает с exception то он ловиться во внешнем цилке и страница запрашивается еще раз
        // Иначе возвращает true если обработатна последняя страница и false если нет
//        try {
//        System.out.println(pURL);

        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .timeout( 30 * 1000)
                .get();

        // Всего по запросу найдено - 1256. На странице записи с 1 по 25

        int iAll  = 0;
        int iFrom = 0;
        int iTo   = 0;

        String headerString = "";

//        setPageType1 = DBUtil.getRegions(1);

        if ( setPageType1.contains(pRegionId)) {
//        if ( pRegionId.equalsIgnoreCase("23")||pRegionId.equalsIgnoreCase("66")  ) {
            headerString = doc.select("body > div > div.lawcase-count").text();
        } else {
            headerString = doc.select("div.style3 > div:nth-child(3)").text();
        }

        headerString = headerString.replace("&nbsp;", "")
                .replaceAll("\t", "")
                .replaceAll("\n", "")
                .trim();

        if ( Str.stEmpty(headerString)) { return true; } // пустая страница. Ничего не найдено

        System.out.println(headerString);

        try {
            Pattern pattern = Pattern.compile("(.*?)(\\d+)(.*?)(\\d+)(.*?)(\\d+)");
            Matcher matcher = pattern.matcher(headerString);
            matcher.find();

            String stAll  = matcher.group(2);
            String stFrom = matcher.group(4);
            String stTo   = matcher.group(6);

            iAll  = Integer.parseInt(stAll);
            iFrom = Integer.parseInt(stFrom);
            iTo   = Integer.parseInt(stTo);

        } catch ( java.lang.IllegalStateException e ) {
            System.out.println("No data found");
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

        for (int rn=2; rn<=iTo-iFrom+2;rn++) {
            LawCase dt = new LawCase();

            if ( setPageType1.contains(pRegionId)) {
                dt.setCaseRegion(pRegionId);
                dt.setCaseCourt( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(1)").text() );

                dt.setCaseNr( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(2) > a").text() );
                dt.setCaseHref( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(2) > a").attr("href") );

                dt.setCaseDt( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(3)").text() );
                dt.setCaseInfo( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(4)").text() );
                dt.setCaseJudge( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(5)").text() );
                dt.setCaseResult( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(6)").text() );
                dt.setCourtAct( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(7) > a").text() );
                dt.setCourtActHref( doc.select("#resultTable > table > tbody > tr:nth-child(" + rn + ") > td:nth-child(7) > a").attr("href") );

            } else {
                dt.setCaseRegion(pRegionId);
                dt.setCaseCourt( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(1)").text() );

                dt.setCaseNr( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(2) > a").text() );
                dt.setCaseHref( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(2) > a").attr("href") );

                dt.setCaseDt( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(3)").text() );
                dt.setCaseInfo( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(4)").text() );
                dt.setCaseJudge( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(5)").text() );
                dt.setCaseResult( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(6)").text() );
                dt.setCourtAct( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(7) > a").text() );
                dt.setCourtActHref( doc.select("#tablcont > tbody > tr:nth-child(" + rn + ") > td:nth-child(7) > a").attr("href") );
            }
            // иногда в url попадается пробел
            dt.setCaseHref( dt.getCaseHref().replace(" ", "") );

            pArray.add(dt);
        }

        if (iAll == iTo) { return true; }

        return false;

    }

}
