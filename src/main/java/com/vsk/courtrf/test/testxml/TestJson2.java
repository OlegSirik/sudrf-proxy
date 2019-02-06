package com.vsk.courtrf.test.testxml;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class TestJson2 {

        public static void readJsonWithObjectMapper(String pURL) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<?,?> resp = objectMapper.readValue(new URL(pURL).openStream(),Map.class);
            String value = (String) resp.get("result");
            if ( !value.isEmpty() ) {

            }
//            for (Map.Entry<?, ?> entry : resp.entrySet())
//            {
//                System.out.println("\n----------------------------\n"+entry.getKey() + "=" + entry.getValue()+"\n");
//            }
        }

        public static void main(String[] args) throws Exception {
            System.setProperty("java.net.useSystemProxies", "true");
            readJsonWithObjectMapper("http://37.230.157.227:8088/area/get-area?lat=55.752379&lng=37.442131");
        }

}
