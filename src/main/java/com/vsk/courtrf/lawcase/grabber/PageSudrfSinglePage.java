package com.vsk.courtrf.lawcase.grabber;

import com.vsk.courtrf.lawcase.entity.LawCase;
import com.vsk.courtrf.lawcase.entity.LawCaseDetails;
import com.vsk.courtrf.util.Str;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.*;

public class PageSudrfSinglePage {

    // Наименование события	Дата	Время	Результат события

    private static Map<String, Integer> headerMap = new HashMap();
    static {
        headerMap.put("Наименование события", 1);
        headerMap.put("Дата", 2);
        headerMap.put("Дата события", 2);
        headerMap.put("Время", 3);
        headerMap.put("Время события", 3);
        headerMap.put("Результат", 4);
        headerMap.put("Результат события", 4);
        headerMap.put("Основания для выбранного результата события", 5);
        headerMap.put("Основание для выбранного результата события", 5);

    }

    public static void main(String[] args) {
        LawCase lc = new LawCase();
//        parsePage("http://leninsky--nsk.sudrf.ru/modules.php?name=sud_delo&name_op=case&case_id=82922152&case_uid=C481621E-FACA-4EAB-8A93-F82E5B293D09&result=0&new=&delo_id=1540005&srv_num=1", lc);
    }

    public static int getTbl2HeaderIdx(String pHeader )  {
        try {
            return headerMap.get(pHeader).intValue();
        } catch (Exception e)  {
            System.out.println(pHeader);
            return 0;
        }
    }

    public static void updateLawCase(int pDelaySec, LawCase lawCase) {
        //System.out.println(new Date() + "::SudRf::Page::"+curRow+"::From::"+pCases.size()+"::ID::" );
        System.out.println(new Date() + "::SudRf::ID::" + lawCase.getId() );
        int curRow = 1;

        curRow++;
        int doCnt = 0;
        boolean doExit = true;

        do {
            try {
                doCnt ++;
                doExit = true;

                parsePage( lawCase );
                Thread.sleep(pDelaySec * 1000);  // чтобы не забанили
                lawCase.setDateSync( new Date());

            } catch (Exception ex) {
                System.out.println(ex);
                doExit = false;
            }
        } while ( !doExit && doCnt < 5 );
    }

    public static void updateLawCases(int pDelaySec, ArrayList<LawCase> pCases) {
        for ( LawCase rc: pCases) {
            updateLawCase(pDelaySec, rc);
        }
    }

    public static void parsePage(LawCase pCase) {
        try {

            System.out.println(pCase.getCaseHref());
            Document doc = Jsoup.connect(pCase.getCaseHref())
                    .timeout(1000*60)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                    .followRedirects(true)
                    .get();

            List<Node> nd = doc.select("body > div.header > div.header__content > div > div > a > h5").get(0).childNodes();
            String courtName = ((TextNode)nd.get(0)).text();
            String caseNr = doc.select("#content > div.casenumber").text().replace("ДЕЛО №","").trim();

            HashMap<String, String> dt = new HashMap();
            dt.clear();

            int rowNr = 1;

            for (int rn=1; rn<30; rn++) {

                String col1 = doc.select("#cont1 > table > tbody > tr:eq(" + rn + ") > td:eq(0)").text();
                String col2 = doc.select("#cont1 > table > tbody > tr:eq(" + rn + ") > td:eq(1)").text();

                if ( Str.stNotEmpty(col1) && Str.stNotEmpty(col2)) {
                    pCase.addDetail( 1, rowNr, col1,col2,"","","","");
                    dt.put(col1,col2);
                    rowNr ++;
                } else {
                    if ( rowNr > 1 ) { break;}
                }
            }

            pCase.setCaseDt( dt.getOrDefault("Дата поступления", pCase.getCaseDt()) );
            pCase.setCaseInfo( dt.getOrDefault( "Категория", pCase.getCaseInfo()));
            pCase.setCaseJudge( dt.getOrDefault( "Председательствующий судья", pCase.getCaseJudge()));
            pCase.setCaseResult( dt.getOrDefault( "Результат рассмотрения", pCase.getCaseResult()));
            pCase.setCaseCourt(courtName);
            pCase.setCaseNr(caseNr);

            rowNr = 1;
            int hdrInd[] = {0,0,0,0,0,0};
            for (int rn=1; rn<30; rn++) {
                String hdrs[] = {"","","","","",""};
                hdrs[0] = "";
                hdrs[1] = Str.stClear( doc.select("#cont2 > table > tbody > tr:eq(" + rn + ") > td:eq(0)").text() );
                hdrs[2] = Str.stClear( doc.select("#cont2 > table > tbody > tr:eq(" + rn + ") > td:eq(1)").text() );
                hdrs[3] = Str.stClear( doc.select("#cont2 > table > tbody > tr:eq(" + rn + ") > td:eq(2)").text() );
                hdrs[4] = Str.stClear( doc.select("#cont2 > table > tbody > tr:eq(" + rn + ") > td:eq(3)").text() );
                hdrs[5] = Str.stClear( doc.select("#cont2 > table > tbody > tr:eq(" + rn + ") > td:eq(4)").text() );

                pCase.addDetail(-2, 0, hdrs[1], hdrs[2], hdrs[3], hdrs[4], hdrs[5], "");
                if (rowNr==1) {
                    if ( Str.stNotEmpty(hdrs[1]) && Str.stNotEmpty(hdrs[2])) {

                        hdrInd[getTbl2HeaderIdx( hdrs[1] )] = 1;
                        hdrInd[getTbl2HeaderIdx( hdrs[2] )] = 2;
                        hdrInd[getTbl2HeaderIdx( hdrs[3] )] = 3;
                        hdrInd[getTbl2HeaderIdx( hdrs[4] )] = 4;
                        hdrInd[getTbl2HeaderIdx( hdrs[5] )] = 5;
                        rowNr ++;
                    }
                }
                else if ( Str.stEmpty(hdrs[1])&&rowNr>1) { break;}
                else {
                    pCase.addDetail(2, rowNr, hdrs[hdrInd[1]], hdrs[hdrInd[2]], hdrs[hdrInd[3]], hdrs[hdrInd[4]], hdrs[hdrInd[5]], "");
                    rowNr ++;
                }
            }

            rowNr = 1;
            for (int rn=1; rn<30; rn++) {
                String col1 = doc.select("#cont3 > table > tbody > tr:eq(" + rn + ") > td:eq(0)").text();
                String col2 = doc.select("#cont3 > table > tbody > tr:eq(" + rn + ") > td:eq(1)").text();

                if ( Str.stNotEmpty(col1) && Str.stNotEmpty(col2)) {
                    pCase.addDetail(3, rowNr, col1, col2, "", "", "", "");
                    rowNr ++;
                } else {
                    if ( rowNr > 1 ) { break;}
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

}
