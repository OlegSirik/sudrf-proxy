package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Court;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class PageSudrfCourtsMir {

    public static void main(String[] args) throws Exception {

        ArrayList<Court> pData = getByRegion("77");
    }


    public static ArrayList<Court> getByRegion(String pRegion  )  {
        ArrayList<Court> pData = new ArrayList<Court>();
        try {
            parsePageJsoup( pData, pRegion );
        } catch (Exception e) {
            System.out.println(e);
        }
        return pData;
    }

    public static void parsePageJsoup(ArrayList<Court>  pData, String pRegion) throws Exception {

        String pURL = "http://msudrf.ru/index.php?id=300"
                + "&&act=go_ms_search&searchtype=ms&var=true&ms_type=ms&court_subj=" + pRegion
                + "&ms_city=&ms_street="
                ;

        System.out.println(pURL);

        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .timeout(60 * 1000)
                .get();

        Elements nd = doc.select("#ms_court_search > table:nth-child(6) > tbody > tr:nth-child(2) > td > table > tbody").get(0).children();


        for (Element elmnt: nd) {

            System.out.println( "CODE=" + elmnt.children().get(0).children().get(0).text()); // code

            Court rec = new Court();

            rec.setName(elmnt.children().get(0).children().get(0).text());
            rec.setCourtType( Court.SUDRFMIR );
            try {

                rec.setCode( elmnt.children().get(0).children().get(1).childNodes().get(4).toString().trim()  );
                rec.setWww(elmnt.children().get(0).children().get(1).children().get(2).children().get(2).text());

            } catch (Exception e) {
                rec.setCourtError(e.toString());
            }

            rec.setCourtType( Court.SUDRFMIR );
            pData.add(rec);


        }

    }

}
