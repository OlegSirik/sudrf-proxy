package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Court;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PageSingleCourt {

    static Logger logger = LoggerFactory.getLogger(PageSingleCourt.class );

    public static void main(String[] args) throws Exception {

    }


    public static void updateDetail( Court[] pData ) {

        for ( Court rc: pData) {
            updateOneSud( rc );
        }
    }

    public static void updateOneSud(Court rc)  {
        String pSudCode = rc.getCode();
        String pSudURL = rc.getWww();
        try {
            String pageURL = pSudURL + "/modules.php?name=terr";
            logger.info( pageURL );
            int pageCount = parsePageJsoup(rc, pageURL);

            for ( int i=2; i<=pageCount; i++ ) {
                pageCount = parsePageJsoup(rc, pSudURL + "/modules.php?name=terr&pagenum="+i );
            }
            rc.setCourtTerrError("OK");
        } catch (Exception e) {
            rc.setCourtTerrError(e.toString());
            logger.info( e.toString() );
            System.out.println(e);
        }}

    public static int parsePageJsoup(Court rc, String pURL) throws Exception {

        System.out.println(pURL);

        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .get();

        Elements nd = doc.select("#tblTerrList > tbody").get(0).children();

        for (Element elmnt: nd) {
            rc.addTerritory( ((Element)elmnt.childNodes().get(0)).text(),  ((Element)elmnt.childNodes().get(1)).text());
        }

        Elements pages = doc.select("#divTerrList > div");
        if ( pages.size() > 0 ) {
            Elements pageNrs = pages.get(0).children();
            return Integer.parseInt( pageNrs.get( pageNrs.size() - 1 ).text() );
        } else {
            return 0;
        }

    }

}
