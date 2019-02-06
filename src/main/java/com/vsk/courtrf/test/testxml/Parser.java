package com.vsk.courtrf.test.testxml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class Parser {

    public static void main(String[] args) throws IOException {

        System.setProperty("java.net.useSystemProxies", "true");

        String out = new Scanner(new URL("https://geocode-maps.yandex.ru/1.x/?geocode=Москва+островная+2").openStream(), "UTF-8").useDelimiter("\\A").next();

        String pos = test2(out);
        if (! pos.equalsIgnoreCase("ERROR") ) {
            String lat = pos.substring(pos.indexOf(" ")+1);
            String lng = pos.substring(0, pos.indexOf(" "));
            String st1 = jsonGetRequest("http://37.230.157.227:8088/area/get-area?lat="+lat+"&lng="+lng);
            //{"result": {"magistrate_id": 205, "description": "<b>\u0410\u0434\u0440\u0435\u0441:</b><br>121359 \u0443\u043b. \u041c\u0430\u0440\u0448. \u0422\u0438\u043c\u043e\u0448\u0435\u043d\u043a\u043e\u0434.32<br>mirsud205@ums-mos.ru<br><b>\u0422\u0435\u043b\u0435\u0444\u043e\u043d \u0437\u0430\u0432\u0435\u0434\u0443\u044e\u0449\u0435\u0433\u043e \u043a\u0430\u043d\u0446\u0435\u043b\u044f\u0440\u0438\u0435\u0439:</b><br>499-149-22-86<br><b>\u0422\u0435\u043b\u0435\u0444\u043e\u043d \u0441\u0435\u043a\u0440\u0435\u0442\u0430\u0440\u044f \u0441\u0443\u0434\u0435\u0431\u043d\u043e\u0433\u043e \u0437\u0430\u0441\u0435\u0434\u0430\u043d\u0438\u044f:</b><br>499-149-16-90\u0444"}}
            String nr = test3(st1);
        }
    }


    private static String getPos(Document doc, XPath xpath) {
        String name = null;
        try {
            XPathExpression expr =
                    xpath.compile("/Point/pos/text()");
            name = (String) expr.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return "ERROR";
        }

        return name;
    }

    public static String test2(String xml) {

        final Pattern pattern = Pattern.compile("<pos>(.+?)</pos>", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(xml);
        matcher.find();
        System.out.println(matcher.group(1));
        return matcher.group(1);

    }

    public static String test3(String json) {

        final Pattern pattern = Pattern.compile("\"magistrate_id\":(.+?),", Pattern.DOTALL);
        final Matcher matcher = pattern.matcher(json);
        matcher.find();
        System.out.println(matcher.group(1));
        return matcher.group(1);

    }

    public static String readJsonWithObjectMapper(String pURL) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<?, ?> resp = objectMapper.readValue(new URL(pURL).openStream(), Map.class);
        String value = (String) resp.get("result");
        if (!value.isEmpty()) {
            return value;
        }
        return "NOTFOUND";
    }
    private static String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }
    public static String jsonGetRequest(String urlQueryString) {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            json = streamToString(inStream); // input stream to string
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}