package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Region;
import com.vsk.courtrf.court.service.RegionServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PageSudrfRegions {

    private static final Logger logger = LoggerFactory.getLogger(PageSudrfRegions.class);

    public static void main(String[] args) throws Exception {

        parsePageJsoup(null );
    }

    public static ArrayList<Region> getRegions() {

        ArrayList<Region> pData = new ArrayList<Region>();
        try {
            parsePageJsoup( pData );
        } catch (Exception e) {
            System.out.println(e);
        }
        return pData;
    }

    public static void parsePageJsoup(ArrayList<Region> pData ) throws Exception {

        String pURL = "http://sudrf.ru/index.php?id=300";
        logger.info("Request URL::" + pURL);

        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .timeout(60 * 1000)
                .get();

        logger.info("Document recieved");

        Elements nd = doc.select("#court_subj").get(0).children();

        //ArrayList<Region> regions = new ArrayList();

        for (Element elmnt: nd) {
            String code = elmnt.attr("value");
            String name = elmnt.text();
            if ( !code.equalsIgnoreCase("0") ) {
                pData.add( new Region(code, name));
            }


        }
    }

}

