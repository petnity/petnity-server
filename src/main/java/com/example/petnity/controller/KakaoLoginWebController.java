package com.example.petnity.controller;

import com.example.petnity.data.dto.KakaoDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.security.JwtManager;
import com.example.petnity.service.KakaoLoginWebService;
import com.example.petnity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/web/kakao")
public class KakaoLoginWebController {

    private final Logger LOGGER = LoggerFactory.getLogger(KakaoLoginWebController.class);


    private final KakaoLoginWebService kakaoLoginWebService;
    private final UserService userService;
    private final JwtManager jwtManager;

    public KakaoLoginWebController(KakaoLoginWebService kakaoLoginWebService,
                                   UserService userService,
                                   JwtManager jwtManager) {
        this.kakaoLoginWebService = kakaoLoginWebService;
        this.userService = userService;
        this.jwtManager = jwtManager;
    }


    @GetMapping(value = "/getKakaoAuthUrl")
    public String getKakaoAuthUrl(HttpServletRequest request) throws Exception{
        String reqUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=9e3f86963761d370e3c36899e8be051d" +
                "&redirect_uri=http://localhost:8080/web/kakao/login" +
                "&response_type=code";

        return reqUrl;
    }


    @GetMapping(value = "/login")
    public ResponseEntity<UserDto.TokenInfo> kakaoLogin(@RequestParam String code) throws IOException {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginWebController] perform {} of Petnity API.", "kakaoLogin");
        LOGGER.info("[KakaoLoginController] Param :: Code = {}.", code);

        KakaoDto.TokenInfo kakaoTokenInfo = kakaoLoginWebService.getKakaoTokenInfo(code);
        LOGGER.info("[KakaoLoginWebController] Token :: kakaoTokenInfo = {}", kakaoTokenInfo);

        UserDto.Info userInfo = kakaoLoginWebService.getUserInfo(kakaoTokenInfo.getAccessToken());
        LOGGER.info("[KakaoLoginWebController] User :: userInfo = {}", userInfo);

        UserDto.Response response = userService.getUserByUserAccount(userInfo.getUserAccount());
        LOGGER.info("[KakaoLoginWebController] Response :: response = {}.", response.toString());

        if (response.getUserId() == null) {
            response = userService.saveUser(userInfo);
            LOGGER.info("[KakaoLoginWebController] Response :: saved User = {}.", response.toString());
        }

        userInfo.setUserId(response.getUserId());
        UserDto.TokenInfo tokenInfo = jwtManager.createTokenInfo(userInfo);
        LOGGER.info("[KakaoLoginWebController] Token :: tokenInfo = {}, Response Time = {}ms", tokenInfo.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }


    @GetMapping(value = "/login/OIDC")
    public ResponseEntity<UserDto.TokenInfo> kakaoLoginByOIDC(@RequestParam String code) throws IOException {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginWebController] perform {} of Petnity API.", "kakaoLoginByOIDC");

        KakaoDto.TokenInfo kakaoTokenInfo = kakaoLoginWebService.getKakaoTokenInfo(code);
        LOGGER.info("[KakaoLoginWebController] Token :: kakaoTokenInfo = {}", kakaoTokenInfo);

        String userInfoByOIDC = kakaoLoginWebService.getUserInfoByOIDC(kakaoTokenInfo.getAccessToken());
        LOGGER.info("[KakaoLoginWebController] Response :: Info = {}", userInfoByOIDC);

        KakaoDto.TokenPayload payload = kakaoLoginWebService.payloadParser(userInfoByOIDC);

        String userAccount = payload.getUserAccount();
        LOGGER.info("[KakaoLoginWebController] Response :: userAccount = {}", userAccount);

        UserDto.Info userInfo = userService.getUserInfoByUserAccount(userAccount);
        LOGGER.info("[KakaoLoginWebController] Response :: userInfo = {}.", userInfo.toString());

        if (userInfo.getUserId() == null) {
            UserDto.Info kakaoUser = UserDto.Info.builder()
                    .userAccount(userAccount)
                    .userEmail("userEmail")
                    .userNickname("userName")
                    .userName("userName")
                    .build();
            LOGGER.info("[KakaoLoginWebController] Response :: kakaoUser = {}.", kakaoUser.toString());
            UserDto.Response response = userService.saveUser(kakaoUser);
            userInfo.setUserId(response.getUserId());
        }

        UserDto.TokenInfo tokenInfo = jwtManager.createTokenInfo(userInfo);
        LOGGER.info("[KakaoLoginWebController] Token :: tokenInfo = {}, Response Time = {}ms", tokenInfo.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }


    @PostMapping(value = "/login/idtoken")
    public ResponseEntity<UserDto.TokenInfo> kakaoLoginByIdToken(@RequestBody KakaoDto.TokenInfo kakaoTokenInfo ) throws IOException {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginWebController] perform {} of Petnity API.", "kakaoLoginByIdToken");

        String idToken = kakaoTokenInfo.getIdToken();
        LOGGER.info("[KakaoLoginWebController] Token :: idToken = {}", idToken);

        KakaoDto.TokenPayload payload = kakaoLoginWebService.idTokenParser(idToken);
        LOGGER.info("[KakaoLoginWebController] Token :: payload = {}", payload.toString());

        Integer expiration = payload.getExpiration();
        LOGGER.info("[UserController] Response :: expiration = {}", expiration);

        if (expiration <= 0) {
            LOGGER.info("[KakaoLoginWebController] Exception :: Id Token Expired. exp = {}", expiration);
            UserDto.TokenInfo badTokenInfo = UserDto.TokenInfo.builder().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badTokenInfo);
        }

        String userAccount = payload.getUserAccount();
        LOGGER.info("[KakaoLoginWebController] Response :: userAccount = {}", userAccount);

        UserDto.Info userInfo = userService.getUserInfoByUserAccount(userAccount);
        LOGGER.info("[KakaoLoginWebController] Response :: userInfo = {}", userInfo.toString());

        UserDto.TokenInfo tokenInfo = jwtManager.createTokenInfo(userInfo);
        LOGGER.info("[KakaoLoginWebController] Token :: tokenInfo = {}, Response Time = {}ms", tokenInfo.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }


    @PostMapping("/agreement")
    public ResponseEntity<String> kakaoAgreementInfo(@RequestBody KakaoDto.TokenInfo kakaoLoginDto) throws IOException {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginWebController] Perform {} of Petnity API.", "kakaoLogout");

        String response = kakaoLoginWebService.getAgreementInfo(kakaoLoginDto.getAccessToken());
        LOGGER.info("[KakaoLoginWebController] Response :: Response Time = {}ms",System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> kakaoLogout(@RequestBody KakaoDto.TokenInfo kakaoLoginDto) {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[KakaoLoginWebController] Perform {} of Petnity API.", "kakaoLogout");

        kakaoLoginWebService.kakaoLogout(kakaoLoginDto.getAccessToken());
        LOGGER.info("[KakaoLoginWebController] Response :: Response Time = {}ms",System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body("Logout");
    }

}
