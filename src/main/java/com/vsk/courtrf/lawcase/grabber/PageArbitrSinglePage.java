package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.service.LawCaseServiceImpl;
import com.vsk.courtrf.util.Str;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PageArbitrSinglePage {

    private static final Logger logger = LoggerFactory.getLogger(LawCaseServiceImpl.class);

    public static void main(String[] args)  {





        //ArrayList dt = new ArrayList();
        //ArrayList<grssudrf.data.SudrfSinglePage> rows = DBUtil.getArbitrForUpdates();

        //ArbitrResult result = parsePrintPage( "74d12cb8-d888-4219-9c8a-906d02c6c047", dt );

    }

    /*
    Обновление статуса дела. Статус есть на странице /Card/
    */
    /*
    public static void updateResults(int pDelaySec) {
        ArrayList<grssudrf.data.SudrfSinglePage> rows = DBUtil.getArbitrForUpdates();

        int i = 1;
        int sleepTime = 120000;

        for (grssudrf.data.SudrfSinglePage rc: rows) {

            int id = rc.rowNr;
            String case_uid = rc.col1;

            System.out.println(new Date() + "::Result::Arbitr row: " + i + " from " + rows.size() + "::ID::"+id+"::UID::"+case_uid);

            int capCnt = 0;
            boolean resultCaptcha = true;

            do {
                ArbitrResult result = parsePage( case_uid );
                resultCaptcha = result.getErrCode().equalsIgnoreCase("CAPTCHA");
                capCnt++;

                if ( resultCaptcha ) {
                    sleepTime = 20 * 60 * 1000;
                    // капча, сон на 20 минут
                    System.out.println("Captcha, sleep 20 min");
                } else {
                    DBUtil.saveArbitrResult( id, result.getCaseResult()  );
                    sleepTime = pDelaySec * 1000; //2 min
                }

                i++;

                try {
                    Thread.sleep(sleepTime);  // чтобы не забанили
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }

                // если капча, то делаем 3 попытки дождаться отмены капчи.
            } while ( resultCaptcha && capCnt < 10 );

        }
    }
*/

    public static void updateLawCase(int pDelaySec, LawCase lawCase) {
//        List<LawCase> pCases = new ArrayList<LawCase>();
//        pCases.add( pCase );
//        updateLawCases( pDelaySec, pCases);

//        int i = 1;
        int sleepTime = 120000;

        int doCnt = 0;
        boolean resultCaptcha = true;

        do {
            parsePrintPage( lawCase.getCaseCode() , lawCase );

            resultCaptcha = lawCase.getLastError().equalsIgnoreCase("CAPTCHA");
            doCnt++;

            if ( resultCaptcha ) {
                sleepTime = 20*60*1000;  // капча, сон на 20 минут
                System.out.println("Captcha, sleep 20 min");
            } else {
                sleepTime = pDelaySec * 1000; //2 min
            }
//            i++;

            try {
                System.out.println( "ARBITR SLEEP ::" + sleepTime);
                Thread.sleep(sleepTime);  // чтобы не забанили
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        } while ( resultCaptcha && doCnt < 10 );

    }

    public static void updateLawCases(int pDelaySec, List<LawCase> pCases) {
        for (LawCase rc: pCases) {
            updateLawCase( pDelaySec, rc);
        }
    }

    public static String getText0(Document doc, String pSel) {
        try {
            return doc.select(pSel).get(0).ownText();
        } catch (Exception e) {
            return "";
        }
    }

    /*
    public static ArbitrResult parsePage(String pUID) {
        ArbitrResult result = new ArbitrResult();
        result.setErrCode("OK");

        try {
            Connection conn = Jsoup.connect("http://kad.arbitr.ru/Card/" + pUID)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header( "Host", "kad.arbitr.ru" )
                    .header( "Connection","keep-alive")
                    //.header( "Content-Length", Integer.toString(bd.length()) )
                    .header( "Accept", "*//*")
                    .header( "Origin" , "http://kad.arbitr.ru")
                    //.header( "x-date-format", "iso")
                    //.header( "X-Requested-With", "XMLHttpRequest")
                    .header( "Content-Type", "text/*; charset=utf-8")
                    .header( "Referer", "http://kad.arbitr.ru/")
                    .header( "Accept-Encoding", "gzip, deflate")
                    .header( "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");

            Document doc = conn.get();

//            String caseNr = doc.select("span.js-case-header-case_num").text();


            result.setCaseResult( doc.select("div.b-case-header-desc").text() );
            //boolean cap = doc.is("div.b-pravocaptcha-row");

            String cp = doc.select("#token").attr("name"); // type="hidden" name="RecaptchaToken" value="">

            if ( cp.equalsIgnoreCase("RecaptchaToken".toUpperCase()) ) {
                result.setErrCode("CAPTCHA");
            }

        } catch ( org.jsoup.HttpStatusException ex ) {
            //System.out.println(ex);
            result.setErrCode("CAPTCHA");
        }
        catch (Exception ex) {
            System.out.println(ex);
            result.setErrCode("ERROR");
        }
        return result;
    }
*/

    public static void parsePrintPage(String pUID, LawCase lawCase) {

        logger.info( "ARBITR::parsePrintPage::START::" + pUID);
        lawCase.setLastError("OK");
        try {

            Document doc = Jsoup.connect("http://kad.arbitr.ru/PrintCard/" + pUID)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header( "Host", "kad.arbitr.ru" )
                    .header( "Connection","keep-alive")
                    //.header( "Content-Length", Integer.toString(bd.length()) )
                    .header( "Accept", "*//*")
                    .header( "Origin" , "http://kad.arbitr.ru")
                    //.header( "x-date-format", "iso")
                    //.header( "X-Requested-With", "XMLHttpRequest")
                    .header( "Content-Type", "text/*; charset=utf-8")
                    .header( "Referer", "http://kad.arbitr.ru/")
                    .header( "Accept-Encoding", "gzip, deflate")
                    .header( "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .get();

            String caseNr = doc.select("#b-case-header > h1").text();
            String caseCategory = doc.select("#case-category").text();

            String caseSide1 = getText0(doc, "#gr_case_partps > table > tbody > tr > td.plaintiffs.first > div > ul > li > span");
            if ( Str.stNotEmpty(caseSide1)) {
                lawCase.addDetail(0, 11, "Истцы", caseSide1, "", "", "", "" );
            }

            String caseSide2 = getText0(doc, "#gr_case_partps > table > tbody > tr > td.defendants > div > ul > li > span");
            if ( Str.stNotEmpty(caseSide2)) {
                lawCase.addDetail(0, 12, "Ответчики", caseSide2, "", "", "", "" );
            }

            String caseSide3 = getText0(doc, "#gr_case_partps > table > tbody > tr > td.third > div > ul > li > span");
            if ( Str.stNotEmpty(caseSide3)) {
                lawCase.addDetail(0, 13, "Третьи лица", caseSide3, "", "", "", "" );
            }

            String caseSide4 = getText0(doc, "#gr_case_partps > table > tbody > tr > td.others > div > ul > li > span");
            if ( Str.stNotEmpty(caseSide4)) {
                lawCase.addDetail(0, 14, "Иные лица", caseSide4, "", "", "", "" );
            }

            String caseDate = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(1) > div.l-col > p.case-date").text();
            String caseType = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(1) > div.l-col > p.case-type").text();
            String caseSubject = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(1) > div.r-col > p > span").text();
            String caseResult = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(1) > div.r-col > h2").text();
            String addInfo = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(2) > div.r-col > span").text();
            String courtName = doc.select("#print > div.b-print-container > div.b-chrono-item-header.page-break > div > div > div.r-col > h4 > span").text();

            String cp = doc.select("#token").attr("name"); // type="hidden" name="RecaptchaToken" value="">

            if ( cp.equalsIgnoreCase("RecaptchaToken".toUpperCase()) ) {
                lawCase.setLastError ("CAPTCHA");
                return;
            }

            lawCase.setCaseCourt( courtName );
            lawCase.setCaseDt( caseDate );
            lawCase.setCaseCategory( caseType );
            lawCase.setCaseResult( caseResult );
            lawCase.setCaseInfo( addInfo );
            lawCase.setCaseJudge( caseSubject );

            int rowNr = 1;

            for (int rn=1; rn<30; rn++) {

                String col11 = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(" + rn + ") > div.l-col > p.case-date").text();
                String col12 = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(" + rn + ") > div.l-col > p.case-type").text();

                String col21 = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(" + rn + ") > div.r-col > p > span").text();
                String col22 = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(" + rn + ") > div.r-col > h2").text();
                String col23 = doc.select("#print > div.b-print-container > div.b-chrono-items-container.page-break > div:nth-child(" + rn + ") > div.r-col > span").text();

                if ( Str.stNotEmpty(col11)) {
                    lawCase.addDetail(1, rn, col11, col12, col21, col22, col23, "" );
                } else { break ; }
            }
            logger.info( "ARBITR::parsePrintPage::FINISHED::" + pUID);
        } catch ( org.jsoup.HttpStatusException ex ) {
            lawCase.setLastError("CAPTCHA");
            logger.error( "ARBITR::parsePrintPage::CAPTCHA::" + pUID);
        } catch (Exception ex) {
            System.out.println(ex);
            lawCase.setLastError("ERROR");
            logger.error( "ARBITR::parsePrintPage::START::" + pUID + "::" + ex.toString());
        }
    }

    /*
    public static ArbitrResult parsePageHer(String pUID) {
        ArbitrResult result = new ArbitrResult();
        result.setErrCode("OK");

        try {


            //Document doc
            Connection conn = Jsoup.connect("http://russud.herokuapp.com/arbitr/" + pUID);
            conn.timeout(120*1000);

            Document doc = conn
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .header( "Host", "russud.herokuapp.com" )
                    .header( "Connection","keep-alive")
                    //.header( "Content-Length", Integer.toString(bd.length()) )
                    .header( "Accept", "*//*")
                    //.header( "Origin" , "http://kad.arbitr.ru")
                    //.header( "x-date-format", "iso")
                    //.header( "X-Requested-With", "XMLHttpRequest")
                    .header( "Content-Type", "text/*; charset=utf-8")
                    //.header( "Referer", "http://kad.arbitr.ru/")
                    .header( "Accept-Encoding", "gzip, deflate")
                    .header( "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .get();

            String bd = doc.select("body").text();
            result.setCaseResult( bd );

            if ( Utl.stEmpty(bd) ) {
                result.setErrCode("CAPTCHA");
            }

            //boolean cap = doc.is("div.b-pravocaptcha-row");

            //String cp = doc.select("#token").attr("name"); // type="hidden" name="RecaptchaToken" value="">

            //if ( cp.equalsIgnoreCase("RecaptchaToken".toUpperCase()) ) {
            //    result.setErrCode("CAPTCHA");
            //}

        } catch ( org.jsoup.HttpStatusException ex ) {
            //System.out.println(ex);
            result.setErrCode("CAPTCHA");
        }
        catch (Exception ex) {
            System.out.println(ex);
            result.setErrCode("ERROR");
        }
        return result;
    }
    */
}
