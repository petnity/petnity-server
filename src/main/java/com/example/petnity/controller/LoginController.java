package com.example.petnity.controller;

import com.example.petnity.data.dto.UserDto;
import com.example.petnity.security.JwtManager;
import com.example.petnity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login-api")
public class LoginController {
    private final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;
    private final JwtManager jwtManager;

    public LoginController(UserService userService, JwtManager jwtManager) {
        this.userService = userService;
        this.jwtManager = jwtManager;
    }


    @PostMapping("/login")
    public ResponseEntity<UserDto.TokenInfo> login(@RequestBody UserDto.Login userLoginDto) {

        LOGGER.info("[LoginController] param = {}.", userLoginDto.toString());

        UserDto.Info userInfo = userService.getUserInfoByUserAccount(userLoginDto.getUserAccount());
        LOGGER.info("[LoginController] User Check :: userInfo={}", userInfo.toString());

        if (userInfo.getUserId() == null) {
            LOGGER.info("[LoginController] Exception :: No Such User");

            UserDto.TokenInfo badTokenInfo = UserDto.TokenInfo.builder().build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badTokenInfo);
        }
        else if (!userLoginDto.getUserPassword().equals(userInfo.getUserPassword())) {
            LOGGER.info("[LoginController] Exception :: Password Miss Match");

            UserDto.TokenInfo badTokenInfo = UserDto.TokenInfo.builder().build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badTokenInfo);
        }

        UserDto.TokenInfo response = jwtManager.createTokenInfo(userInfo);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody UserDto.TokenInfo userTokenInfoDto) {
        LOGGER.info("[LoginController] Perform {} of Petnity API.", "logout");
        LOGGER.info("[LoginController] param = {}.", userTokenInfoDto.toString());


        return ResponseEntity.status(HttpStatus.OK).body("Logout");
    }



}
