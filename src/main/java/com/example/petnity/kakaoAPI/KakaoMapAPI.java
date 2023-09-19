package com.example.petnity.kakaoAPI;

import com.example.petnity.data.dto.LocateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class KakaoMapAPI {
    private final Logger LOGGER = LoggerFactory.getLogger(KakaoMapAPI.class);
    private final LocateDTO locateDTO;

    public KakaoMapAPI(LocateDTO locateDTO){
        this.locateDTO = locateDTO;
    }

    public String get_list(){
        LOGGER.info("[KakaoMapAPI] perform {} of Petnity API.", "get_list");

        String restkey = "9e3f86963761d370e3c36899e8be051d";
        String kakaourl = "https://dapi.kakao.com/v2/local/search/keyword.json";
        //?y=37.514322572335935&x=127.06283102249932&radius=20000"
        String responseData = "";
        String returnData = "";
        BufferedReader br = null;
        try {
            String encodeResult = URLEncoder.encode(locateDTO.getKeyword_search(), "UTF-8");
            URL url = new URL(kakaourl + "?" + "y=" + locateDTO.getLocate_y() +"&x=" + locateDTO.getLocate_x() +"&page="+locateDTO.getPage()+"&radius=20000"+"&query=" + encodeResult);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + restkey);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            StringBuffer sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : " + responseCode);
            System.out.println("http 응답 데이터 : " + returnData);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다ㄱㅡㄴㄷㅔ
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            LOGGER.info("[KakaoMap] Response :: response = {}", returnData);
            return returnData;
        }
    }
}
