package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.util.Str;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.nio.charset.StandardCharsets;

public class Court77MSTerr {
    /*
    Поиск тер подсудности по адресу.
    Для москвы.
    Получение координат дома через яндект
    Далее по координатам получаем суд.
    */

    static Logger logger = LoggerFactory.getLogger(Court77MSTerr.class );

    private static String getYandexPos(String address) {
        try {
            String xml = new Scanner(new URL("https://geocode-maps.yandex.ru/1.x/?geocode=Москва+островная+2").openStream(), "UTF-8").useDelimiter("\\A").next();

            final Pattern pattern = Pattern.compile("<pos>(.+?)</pos>", Pattern.DOTALL);
            final Matcher matcher = pattern.matcher(xml);
            matcher.find();
            System.out.println(matcher.group(1));
            return matcher.group(1);

        } catch (Exception e) {
            logger.error(e.toString());
        }
        return "";
    }

    private static String getJsonByPos(String lat, String lng) {
        try {
            //String json = new Scanner(new URL("http://37.230.157.227:8088/area/get-area?lat="+lat+"&lng=" + lng).openStream(), "UTF-8").useDelimiter("\\A").next();
            Document doc = Jsoup.connect("http://37.230.157.227:8088/area/get-area?lat="+lat+"&lng="+lng)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                   // .header( "Host", "kad.arbitr.ru" )
                   // .header( "Connection","keep-alive")
//                    .header( "Content-Length", Integer.toString( pBody.length()) )
                    .header( "Accept", "*/*")
                 //   .header( "Origin" , "http://kad.arbitr.ru")
                    .header( "x-date-format", "iso")
                    .header( "X-Requested-With", "XMLHttpRequest")
                   // .header( "Content-Type", "application/json")
                    .header( "Referer", "http://kad.arbitr.ru/")
                    .header( "Accept-Encoding", "gzip, deflate")
                    .header( "Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
  //                  .requestBody( pBody )
                    .get();
String s = doc.text();

            String utf8Text = s;
            byte[] bytes = utf8Text.getBytes(StandardCharsets.UTF_8);
            String text = new String(bytes, StandardCharsets.UTF_8);

            String st2 = Str.fromUnicode(s);

            String dummy = "2";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.setProperty("java.net.useSystemProxies", "true");

        String pos = getYandexPos("asd");
        String pos1 = pos.substring(0, pos.indexOf(" "));
        String pos2 = pos.substring(pos.indexOf(" ")+1);
        String res = getJsonByPos(pos2, pos1);
    }
}
