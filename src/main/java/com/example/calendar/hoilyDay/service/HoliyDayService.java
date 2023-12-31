package com.example.calendar.hoilyDay.service;


import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.hoilyDay.dto.HoliyDayRequestDto;
import com.example.calendar.hoilyDay.entity.Holiday;
import com.example.calendar.hoilyDay.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource("classpath:application-key.properties")
@RequiredArgsConstructor
public class HoliyDayService {
    private final HolidayRepository holidayRepository;

    @Value("${api.secret.key}")
    private String API_SERVICE_KEY;

    @Transactional
    public List<HoliyDayRequestDto> holiydata(String month, String year) throws IOException, ParserConfigurationException, SAXException {

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + API_SERVICE_KEY); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("40", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("solYear", "UTF-8") + "=" + URLEncoder.encode(year, "UTF-8")); /*연*/
        urlBuilder.append("&" + URLEncoder.encode("solMonth", "UTF-8") + "=" + URLEncoder.encode(month, "UTF-8")); /*월*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String xml = sb.toString();

//        //xml데이터를 json데이터로 변환
//        JSONObject jObject = XML.toJSONObject(xml);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        Object json = mapper.readValue(jObject.toString(), Object.class);

        // XML을 파싱하여 객체 리스트로 저장
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        NodeList itemList = document.getElementsByTagName("item");

        List<HoliyDayRequestDto> holiydayList = new ArrayList<>();

        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String dateName = item.getElementsByTagName("dateName").item(0).getTextContent();
            String locdate = item.getElementsByTagName("locdate").item(0).getTextContent();

            HoliyDayRequestDto info = new HoliyDayRequestDto(dateName, locdate);
            holiydayList.add(info);
        }
//        for(HoliyDayRequestDto holiyDayRequestDto : holiydayList){
//            Holiday holiday = new Holiday(holiyDayRequestDto);
//            holidayRepository.saveAndFlush(holiday);
//        }
            return holiydayList;
    }
    @Transactional
    public List<HoliyDayRequestDto> getHoliydata(String month, String year) {
        List<HoliyDayRequestDto> holiyDayRequestDtoList = holidayRepository.findAllByYearAndMonth(year,month);
        return holiyDayRequestDtoList;
    }
    //공공데이터
    public ResponseEntity<Message> test(String month, String year, UserDetailsImpl userDetails) throws IOException, ParserConfigurationException, SAXException {
        return new ResponseEntity<>(new Message("공공데이터", holiydata(month, year)), HttpStatus.OK);
    }
    }