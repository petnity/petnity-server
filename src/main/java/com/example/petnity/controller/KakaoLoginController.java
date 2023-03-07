package com.example.petnity.controller;

import com.example.petnity.data.dto.KakaoDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.security.JwtManager;
import com.example.petnity.service.UserService;
import com.example.petnity.service.KakaoLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/kakaologin-api")
public class KakaoLoginController {

    private final Logger LOGGER = LoggerFactory.getLogger(KakaoLoginController.class);
    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;
    private final JwtManager jwtManager;

    public KakaoLoginController(UserService userService,
                                KakaoLoginService kakaoLoginService,
                                JwtManager jwtManager) {
        this.userService = userService;
        this.kakaoLoginService = kakaoLoginService;
        this.jwtManager = jwtManager;
    }


    @PostMapping(value = "/login")
    public ResponseEntity<UserDto.TokenInfo> kakaoLogin(@RequestBody KakaoDto.TokenInfo kakaoTokenInfo) throws IOException {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginController] Perform {} of Petnity API.", "kakaoLogin");
        LOGGER.info("[KakaoLoginController] Param :: kakaoTokenInfo = {}.", kakaoTokenInfo.toString());

        UserDto.Info userInfo = kakaoLoginService.getUserInfo(kakaoTokenInfo.getAccessToken());
        LOGGER.info("[KakaoLoginController] User :: userInfo = {}.", userInfo.toString());

        UserDto.Response response = userService.getUserByUserAccount(userInfo.getUserAccount());
        LOGGER.info("[KakaoLoginController] Response :: response = {}.", response.toString());

        if (response.getUserId() == null) {
            response = userService.saveUser(userInfo);
            LOGGER.info("[KakaoLoginController] Response :: saved response = {}", response.toString());
        }

        userInfo.setUserId(response.getUserId());
        UserDto.TokenInfo tokenInfo = jwtManager.createTokenInfo(userInfo);
        LOGGER.info("[KakaoLoginController] Token :: tokenInfo = {}, Response Time = {}ms", tokenInfo.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> kakaoLogout(@RequestBody KakaoDto.TokenInfo kakaoTokenInfo) {
        long StartTime = System.currentTimeMillis();
        LOGGER.info("[KakaoLoginController] Perform {} of Petnity API.", "kakaoLogout");

        kakaoLoginService.kakaoLogout(kakaoTokenInfo.getAccessToken());
        LOGGER.info("[KakaoLoginController] Response :: Response Time = {}ms",System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body("Logout");
    }

}
