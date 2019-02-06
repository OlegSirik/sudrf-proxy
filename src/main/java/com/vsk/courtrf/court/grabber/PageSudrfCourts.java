package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.entity.Court;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PageSudrfCourts {

    public static void main(String[] args) throws Exception {
        ArrayList<Court> pData = getByRegion("77");
    }



    public static ArrayList<Court> getByRegion(String pRegion )  {
        ArrayList<Court> pData = new ArrayList<Court>();
        try {
            parsePageJsoup( pData, pRegion );
        } catch (Exception e) {
            System.out.println(e);
        }
        return pData;
    }

    private static void parsePageJsoup( ArrayList<Court>  pData, String pRegion) throws Exception {

        String pURL = "http://sudrf.ru/index.php?id=300"
                + "&id=300&act=go_search&searchtype=fs&court_name=&court_subj=" + pRegion
                + "&fs_city=&fs_street=&court_type=0&court_okrug=0&vcourt_okrug=0"
                ;
        System.out.println(pURL);
        Document doc = Jsoup.connect(pURL)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                .timeout(60 * 1000)
                .get();

        Elements nd = doc.select("#courtGuideTbl > tbody > tr:nth-child(10) > td > div > ul").get(0).children();

        for (Element elmnt: nd) {
            List<Node> tst = elmnt.children().get(1).childNodes();

            System.out.println( elmnt.children().get(0).text()); // code

            Court rec = new Court();

            rec.setName(elmnt.children().get(0).text());
            try {
                rec.setAddress(tst.get(7).toString());
                rec.setCode(tst.get(4).toString().trim());
                rec.setEmail(((Element) tst.get(15)).text());
                rec.setKray(((Element) tst.get(2)).text());
                rec.setOkrug(((Element) tst.get(0)).text());//0
                rec.setPhone(tst.get(10).toString());
                rec.setWww(((Element) tst.get(tst.size() - 1).childNode(3)).text());

                rec.setCourtError("OK");
                rec.setCourtType( Court.SUDRF );

            } catch (Exception e) {
                rec.setCourtError( e.toString() );
            }

            pData.add(rec);
        }
    }

}
