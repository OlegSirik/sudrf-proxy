package com.vsk.courtrf.court.grabber;

import com.vsk.courtrf.court.grabber.court77rs.Resp;
import com.vsk.courtrf.court.grabber.court77rs.SrcResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class Court77RSTerr {
    /*
    Получение подсудности по адресу в москве.
    Сначала поиск по локальной БД
    Если нет, то поиск онлайн по сайту
     */

    public static String[] getStreets4Street(String street) {
        String url = "https://www.mos-gorsud.ru/territorial/getStreets?term="+ street;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String[]> resp = restTemplate.getForEntity(url, String[].class );
        return resp.getBody();
    }

    public static List<SrcResult> getCourttCodes(String street) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("query", street ); //"Тихвинская ул.");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


        String url = "https://www.mos-gorsud.ru/territorial/search";
        RestTemplate restTemplate = new RestTemplate();

        try {
            Resp resp = restTemplate.postForObject(url, request, Resp.class);
            ArrayList<SrcResult> courts = new ArrayList();

            for (int i = 0; i < resp.getChildren().size(); i++) {
                String courtCode = resp.getChildren().get(i).getArea_courts().get(0).getCourt().getCode();
                String houses = resp.getChildren().get(i).getDisplay_name();

                SrcResult rslt = new SrcResult(houses,courtCode);
                courts.add( rslt );
                //courts.add( resp.getChildren().get(0).getArea_courts().get(i).getCourt().getCode());
            }
            return courts;
        } catch ( Exception e) { System.out.println (e);}
        return null;
    }

}
