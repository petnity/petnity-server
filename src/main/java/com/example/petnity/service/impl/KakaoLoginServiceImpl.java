package com.example.petnity.service.impl;

import com.example.petnity.data.dto.KakaoDto;
import com.example.petnity.data.dto.UserDto;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoLoginServiceImpl implements com.example.petnity.service.KakaoLoginService {
    private final Logger LOGGER = LoggerFactory.getLogger(KakaoLoginServiceImpl.class);

    @Override
    public KakaoDto.TokenInfo getKakaoTokenInfo(String code) throws IOException {
        LOGGER.info("[KakaoLoginService] perform {} of Petnity API.", "getKakaoTokenInfo");
        LOGGER.info("[KakaoLoginService] Param :: code = {}", code);

        Map<String, Object> tokenInfo = new HashMap<>();

        String accessToken = "";
        String refreshToken = "";
        String idToken = "";
        Integer expiresIn = 0;
        Integer refreshTokenExpiresIn = 0;

        String host = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=fd3786d57849589d07ee5f9e8ec1ef8d");
            sb.append("&redirect_uri=http://localhost:8080/web/kakao/login");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            LOGGER.info("[KakaoLoginService] Response :: response code = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String responseBody = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                responseBody += line;
            }
            LOGGER.info("[KakaoLoginService] Response :: response body = {}", responseBody);


            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(responseBody);

            Gson gson =  new GsonBuilder().setPrettyPrinting().create();

            LOGGER.info("[KakaoLoginService] Response :: element = \n{}", gson.toJson(element));

            accessToken = element.getAsJsonObject().get("accessToken").getAsString();
            refreshToken = element.getAsJsonObject().get("refreshToken").getAsString();

            expiresIn = element.getAsJsonObject().get("expiresIn").getAsInt();
            refreshTokenExpiresIn = element.getAsJsonObject().get("refreshTokenExpiresIn").getAsInt();

            LOGGER.info("[KakaoLoginService] Response :: accessToken = {}", accessToken);
            LOGGER.info("[KakaoLoginService] Response :: refreshToken = {}", refreshToken);
            LOGGER.info("[KakaoLoginService] Response :: expiresIn = {}", expiresIn);
            LOGGER.info("[KakaoLoginService] Response :: refreshTokenExpiresIn = {}", refreshTokenExpiresIn);

            tokenInfo.put("idToken", idToken);
            tokenInfo.put("accessToken", accessToken);
            tokenInfo.put("expiresIn", expiresIn);
            tokenInfo.put("refreshToken", refreshToken);
            tokenInfo.put("refreshTokenExpiresIn", refreshTokenExpiresIn);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        KakaoDto.TokenInfo kakaoTokenInfo = KakaoDto.TokenInfo.builder()
                .idToken((String) tokenInfo.get("idToken"))
                .accessToken((String) tokenInfo.get("accessToken"))
                .build();

        return kakaoTokenInfo;
    }

    @Override
    public UserDto.Info getUserInfo(String accessToken) throws IOException {
        LOGGER.info("[KakaoLoginService] perform {} of Petnity API.", "getUserInfo");
        LOGGER.info("[KakaoLoginService] Param :: accessToken = {}", accessToken);

        String host = "https://kapi.kakao.com/v2/user/me";
        Map<String, Object> userInfoMap = new HashMap<>();

        try {
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = urlConnection.getResponseCode();
            LOGGER.info("[KakaoLoginService] Response :: response code = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String responseBody = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                responseBody += line;
            }
            LOGGER.info("[KakaoLoginService] Response :: response body = {}", responseBody);

            JsonParser parser = new JsonParser();
            JsonElement responseElement = parser.parse(responseBody);
            JsonObject responseObject = responseElement.getAsJsonObject();

            Gson gson =  new GsonBuilder().setPrettyPrinting().create();
            LOGGER.info("[KakaoLoginService] Response :: element = \n{}", gson.toJson(responseObject));


            JsonObject properties = responseObject.get("properties").getAsJsonObject();
//            LOGGER.info("[KakaoLoginService] Response :: properties = \n{}", gson.toJson(properties));

//            String nickname = properties.get("nickname").getAsString();
//            LOGGER.info("[KakaoLoginService] Response :: nickname = {}", nickname);

            JsonObject kakaoAccount = responseObject.get("kakao_account").getAsJsonObject();
//            LOGGER.info("[KakaoLoginService] Response :: kakaoAccount = \n{}", gson.toJson(kakaoAccount));

            JsonObject profile = kakaoAccount.get("profile").getAsJsonObject();
//            LOGGER.info("[KakaoLoginService] Response :: profile = \n{}", gson.toJson(profile));

            String userAccount = responseObject.get("id").getAsString();
            LOGGER.info("[KakaoLoginService] Response :: userAccount = {}", userAccount);

            String nickname = profile.get("nickname").getAsString();
            LOGGER.info("[KakaoLoginService] Response :: nickname = {}", nickname);

            String thumbnailImageUrl = profile.get("thumbnail_image_url").getAsString();
            LOGGER.info("[KakaoLoginService] Response :: thumbnailImageUrl = {}", thumbnailImageUrl);

            String profileImageUrl = profile.get("profile_image_url").getAsString();
            LOGGER.info("[KakaoLoginService] Response :: profileImageUrl = {}", profileImageUrl);

            String email = "";
            boolean hasEmail = kakaoAccount.get("has_email").getAsBoolean();
            if (hasEmail) {
                email = kakaoAccount.get("email").getAsString();
            }
            LOGGER.info("[KakaoLoginService] Response :: email = {}", email);

            String gender = "";
            boolean hasGender = kakaoAccount.get("has_gender").getAsBoolean();
            if (hasGender) {
                gender = kakaoAccount.get("gender").getAsString();
            }
            LOGGER.info("[KakaoLoginService] Response :: gender = {}", gender);

            String birthday = "";
            boolean hasBirthday = kakaoAccount.get("has_birthday").getAsBoolean();
            if (hasBirthday) {
                birthday = kakaoAccount.get("birthday").getAsString();
            }
            LOGGER.info("[KakaoLoginService] Response :: birthday = {}", birthday);

            userInfoMap.put("userAccount", userAccount);
            userInfoMap.put("email", email);
            userInfoMap.put("nickname", nickname);
            userInfoMap.put("thumbnailImageUrl", thumbnailImageUrl);
            userInfoMap.put("profileImageUrl", profileImageUrl);
            userInfoMap.put("gender", gender);
            userInfoMap.put("birthday", birthday);

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDto.Info userDtoInfo = UserDto.Info.builder()
                .userAccount((String) userInfoMap.get("id"))
                .userEmail((String) userInfoMap.get("email"))
                .userNickname((String) userInfoMap.get("nickname"))
                .userPassword("")
                .userThumbnailImageUrl((String) userInfoMap.get("thumbnailImageUrl"))
                .userProfileImageUrl((String) userInfoMap.get("profileImageUrl"))
                .userGender((String) userInfoMap.get("gender"))
                .userBirthDay((String) userInfoMap.get("birthday"))
                .build();

        return userDtoInfo;
    }


    @Override
    public String getUserInfoByOIDC(String accessToken) {
        LOGGER.info("[KakaoLoginService] perform {} of Petnity API.", "getUserInfoByOIDC");

        String responseBody = "";
        String host = "https://kapi.kakao.com/v1/oidc/userinfo";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = urlConnection.getResponseCode();
            LOGGER.info("[KakaoLoginService] Response :: response code = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null) {
                responseBody += line;
            }
            LOGGER.info("[KakaoLoginService] Response :: response body = {}", responseBody);

            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }


    @Override
    public String getAgreementInfo(String accessToken) {
        LOGGER.info("[KakaoLoginService] perform {} of Petnity API.", "getAgreementInfo");

        String responseBody = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = urlConnection.getResponseCode();
            LOGGER.info("[KakaoLoginService] Response :: response code = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line = br.readLine()) != null) {
                responseBody += line;
            }
            LOGGER.info("[KakaoLoginService] Response :: response body = {}", responseBody);

            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseBody;
    }


    @Override
    public void kakaoLogout(String accessToken) {
        LOGGER.info("[KakaoLoginService] perform {} of Petnity API.", "kakaoLogout");

        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            LOGGER.info("[KakaoLoginService] Response :: response code = {}", responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String responseBody = "";
            String line = "";

            while ((line = br.readLine()) != null) {
                responseBody += line;
            }
            LOGGER.info("[KakaoLoginService] Response :: response body = {}", responseBody);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
