package com.vsk.courtrf.court.grabber.court77rs;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

public class test {
    public static void main(String args[]) {

        test2("Тихвинская");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("query", "Тихвинская ул.");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


    String url = "https://www.mos-gorsud.ru/territorial/search";
        RestTemplate restTemplate = new RestTemplate();
        Resp resp = restTemplate.postForObject ( url, request , Resp.class );
        //Resp resp = restTemplate.getForObject("http://graph.facebook.com/pivotalsoftware", Resp.class);
        //System.out.println("Name:    " + resp.getId() );
        String str = "123";
    }

    public static String[] test2(String str) {
        String url = "https://www.mos-gorsud.ru/territorial/getStreets?term="+ str;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String[]> resp = restTemplate.getForEntity(url, String[].class );
        String str1 = "123";
        return null;
    }
}
