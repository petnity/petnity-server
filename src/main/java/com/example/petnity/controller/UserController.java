package com.example.petnity.controller;

import com.example.petnity.data.dto.PetDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.security.JwtManager;
import com.example.petnity.service.KakaoLoginWebService;
import com.example.petnity.service.PetService;
import com.example.petnity.service.UserService;
import com.example.petnity.service.KakaoLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/user-api")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final PetService petService;
    private final KakaoLoginService kakaoLoginService;
    private final KakaoLoginWebService kakaoLoginWebService;
    private final JwtManager jwtManager;


    public UserController(UserService userService,
                          PetService petService,
                          KakaoLoginService kakaoLoginService,
                          KakaoLoginWebService kakaoLoginWebService,
                          JwtManager jwtManager) {
        this.userService = userService;
        this.petService = petService;
        this.kakaoLoginService = kakaoLoginService;
        this.kakaoLoginWebService = kakaoLoginWebService;
        this.jwtManager = jwtManager;
    }


    @PostMapping("/user/signup")
    public ResponseEntity<UserDto.TokenInfo> createUser(@Valid @RequestBody UserDto.Info userInfoDto){
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[UserController] perform {} of Petnity API.", "createUser");
        LOGGER.info("[UserController] Param :: userInfoDto = {}", userInfoDto.toString());

        UserDto.Response signedUser = userService.getUserByUserAccount(userInfoDto.getUserAccount());
        if (signedUser.getUserId() != null) {
            LOGGER.warn("[UserController] Exception :: User Account Duplicatied");

            UserDto.TokenInfo badTokenInfo = UserDto.TokenInfo.builder().build();
            LOGGER.info("[UserController] Token :: badTokenInfo = {}, Response Time = {}ms", badTokenInfo.toString(), System.currentTimeMillis() - StartTime);
            return ResponseEntity.status(HttpStatus.OK).body(badTokenInfo);
        }

        UserDto.Response savedUser = userService.saveUser(userInfoDto);
        LOGGER.info("[LoginController] User :: savedUser = {}", savedUser);

        userInfoDto.setUserId(savedUser.getUserId());
        UserDto.TokenInfo tokenInfo = jwtManager.createTokenInfo(userInfoDto);
        LOGGER.info("[UserController] Token :: tokenInfo = {}, Response Time = {}ms", tokenInfo.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(tokenInfo);
    }


    @PostMapping("/user")
    public ResponseEntity<UserDto.Response> getUserByToken(@RequestBody UserDto.TokenInfo userTokenInfoDto) {
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[UserController] Perform {} of Petnity API.", "getUserByToken");
        LOGGER.info("[UserController] Param :: userTokenInfoDto = {}", userTokenInfoDto);

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);
        LOGGER.info("[UserController] Token :: checkedAccessToken = {}", checkedAccessToken);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[UserController] Exception :: Token Expired");

            UserDto.Response badResponse = UserDto.Response.builder().build();
            LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", badResponse.toString(), System.currentTimeMillis() - StartTime);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[UserController] Token :: Valid Token");

        Long userId = jwtManager.getUserIdFromToken(checkedAccessToken);
        LOGGER.info("[UserController] User :: userId = {}", userId);

        UserDto.Response response = userService.getUserByUserId(userId);
        response.setAccessToken(checkedAccessToken);
        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/user/pets")
    public ResponseEntity<UserDto.PetList> getPetsByToken(@RequestBody UserDto.TokenInfo userTokenInfoDto){
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[UserController] perform {} of Petnity API.", "getPetByToken");
        LOGGER.info("[UserController] Param :: userTokenInfoDto = {}", userTokenInfoDto);

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[UserController] Exception :: Token Expired");

            UserDto.PetList badResponse = UserDto.PetList.builder().build();
            LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", badResponse.toString(), System.currentTimeMillis() - StartTime);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[UserController] Token :: Valid Token");

        Long userId = jwtManager.getUserIdFromToken(checkedAccessToken);
        LOGGER.info("[UserController] User :: userId = {}", userId);

        UserDto.Response userResponse = userService.getUserByUserId(userId);
        LOGGER.info("[UserController] User :: userResponse = {}", userResponse.toString());

        List<PetDto.Response> petDtoResponseList = userResponse.getPetDtoResponseList();
        petDtoResponseList.forEach(petDto -> {
            LOGGER.info("[UserController] Response :: petDto = {}", petDto.toString());
        });

        UserDto.PetList response = UserDto.PetList.builder().build();
        response.setAccessToken(checkedAccessToken);

        // TODO Add new access token in Response
        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response, System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping(value = "/user/update")
    public ResponseEntity<UserDto.Response> updateUser(@Valid @RequestBody UserDto.Request userDto){
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[UserController] perform {} of Petnity API.", "updateUser");
        LOGGER.info("[UserController] Param :: userDtoInfo = {}", userDto.toString());

        UserDto.TokenInfo userTokenInfoDto = UserDto.TokenInfo.builder()
                .accessToken(userDto.getAccessToken())
                .refreshToken(userDto.getRefreshToken())
                .build();

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[UserController] Exception :: Token Expired");

            UserDto.Response badResponse = UserDto.Response.builder().build();
            LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", badResponse.toString(), System.currentTimeMillis() - StartTime);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[UserController] Token :: Valid Token");

        Long userId = jwtManager.getUserIdFromToken(checkedAccessToken);
        String userAccount = userService.getUserByUserId(userId).getUserAccount();

        // Can't Change User Account in this way
        if (userDto.getUserId() != userId || !userDto.getUserAccount().equals(userAccount)) {
            LOGGER.warn("[UserController] Exception :: Unmatched User");

            UserDto.Response badResponse = UserDto.Response.builder().build();
            LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", badResponse.toString(), System.currentTimeMillis() - StartTime);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badResponse);
        }

        UserDto.Response response = userService.saveUser(userDto);
        response.setAccessToken(checkedAccessToken);
        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response.toString(), System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping(value = "/userId/delete")
    public ResponseEntity<String> deleteUseByUserId(@RequestBody UserDto.TokenInfo userTokenInfoDto){
        long StartTime = System.currentTimeMillis();

        LOGGER.info("[UserController] perform {} of Petnity API.", "deleteUserbyUserId");
        LOGGER.info("[UserController] Param :: userTokenInfoDto = {}", userTokenInfoDto);

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[UserController] Exception :: Token Expired");

            String badResponse = "Token Expired";
            LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", badResponse, System.currentTimeMillis() - StartTime);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[UserController] Token :: Valid Token");

        Long userId = jwtManager.getUserIdFromToken(checkedAccessToken);
        LOGGER.info("[UserController] User :: userId = {}", userId);

        userService.deleteUserByUserId(userId);

        String response = userId + " is deleted";
        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response, System.currentTimeMillis() - StartTime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//    @PostMapping("/user/kakao")
//    public ResponseEntity<UserDto.Response> getUserByKakaoIdToken(@RequestBody KakaoDto.LoginRequest kakaoLoginDto) {
//        LOGGER.info("[UserController] Perform {} of Petnity API.", "getUserByKakaoIdToken");
//
//        String idToken = kakaoLoginDto.getIdToken();
//        LOGGER.info("[UserController] Response :: idToken : {}", idToken);
//
//        Map<String, Object> payload = kakaoLoginWebService.idTokenParser(idToken);
//
//        payload.entrySet().forEach(info -> {
//            LOGGER.info("[KakaoLoginWebController] Response ::  Payload {} : {}", info.getKey(), info.getValue());
//        });
//
//        Integer exp = (Integer) payload.get("exp");
//        LOGGER.info("[UserController] Response :: exp = {}", exp);
//
//        if (exp < 0) {
//            LOGGER.info("[UserController] Exception :: Id Token Expired. exp={}", exp);
//            UserDto.Response badResponse = UserDto.Response.builder().build();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badResponse);
//        }


//        String userAccount = (String) payload.get("sub");
//        LOGGER.info("[UserController] Response :: userAccount = {}", userAccount);
//
//        UserDto.Response response = userService.getUserByUserAccount(userAccount);
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", response.toString());
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @GetMapping("/userId/{userId}")
//    public ResponseEntity<UserDto.Response> getUserById(@PathVariable Long userId){
//        LOGGER.info("[UserController] Perform {} of Petnity API.", "getUserById");
//        UserDto.Response response = userService.getUserByUserId(userId);
//
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", response.toString());
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @GetMapping("/userEmail/{userEmail}")
//    public ResponseEntity<UserDto.Response> getUserByUserEmail(@PathVariable String userEmail) {
//        LOGGER.info("[UserController] Perform {} of Petnity API.", "getUserByEmail");
//        UserDto.Response response = userService.getUserByUserEmail(userEmail);
//
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", response.toString());
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @GetMapping("/userId/{userId}/pets")
//    public ResponseEntity<List<PetDto.Response>> getPetsByUserId(@PathVariable Long userId){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "getPetByUserId");
//
//        UserDto.Response userResponse = userService.getUserByUserId(userId);
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", userResponse.toString());
//
//        List<PetDto.Response> petDtoResponseList = userResponse.getPetDtoResponseList();
//        petDtoResponseList.forEach(petDto -> {
//            LOGGER.info("[UserController] Response :: petDto = {}", petDto.toString());
//        });
//
//        return ResponseEntity.status(HttpStatus.OK).body(petDtoResponseList);
//    }


//    @GetMapping("/userEmail/{userEmail}/pets")
//    public ResponseEntity<List<PetDto.Response>> getPetsByUserEmail(@PathVariable String userEmail){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "getPetByUserEmail");
//
//        UserDto.Response userResponse = userService.getUserByUserEmail(userEmail);
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", userResponse.toString());
//
//        List<PetDto.Response> petDtoResponseList = userResponse.getPetDtoResponseList();
//        petDtoResponseList.forEach(petDto -> {
//            LOGGER.info("[UserController] Response :: petDto = {}", petDto.toString());
//        });
//
//        return ResponseEntity.status(HttpStatus.OK).body(petDtoResponseList);
//    }


//    @PostMapping(value = "/user/update")
//    public ResponseEntity<UserDto.Response> updateUser(@Valid @RequestBody UserDto.Request userDto){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "updateUser");
//
//        long StartTime = System.currentTimeMillis();
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", userDto.toString());
//
//        UserDto.Response response = userService.saveUser(userDto);
//
//        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response.toString(), System.currentTimeMillis() - StartTime);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @DeleteMapping(value = "/userId/{userId}/delete")
//    public ResponseEntity<String> deleteUseByUserId(@PathVariable Long userId){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "deleteUserbyUserId");
//
//        long StartTime = System.currentTimeMillis();
//        LOGGER.info("[UserController] Response :: id = {}", userId);
//
//        userService.deleteUserByUserId(userId);
//
//        String response = userId + " is deleted";
//
//        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response, System.currentTimeMillis() - StartTime);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @DeleteMapping(value = "/userEmail/{userEmail}/delete")
//    public ResponseEntity<String> deleteUserByUserEmail(@PathVariable String userEmail){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "deleteByUserEmail");
//
//        long StartTime = System.currentTimeMillis();
//        LOGGER.info("[UserController] Response :: userEmail = {}", userEmail);
//
//        userService.deleteUserByUserEmail(userEmail);
//
//        String response = userEmail + " is deleted";
//
//        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response, System.currentTimeMillis() - StartTime);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }


//    @PostMapping(value = "/user/updateUserInfo")
//    public ResponseEntity<UserDto.Response> updateUserInfo(@Valid @RequestBody UserDto.Info userDto){
//        LOGGER.info("[UserController] perform {} of Petnity API.", "updateUserInfo");
//
//        long StartTime = System.currentTimeMillis();
//        LOGGER.info("[UserController] Response :: userDtoInfo = {}", userDto.toString());
//
//        UserDto.Response response = userService.saveUser(userDto);
//
//        LOGGER.info("[UserController] Response :: response = {}, Response Time = {}ms", response.toString(), System.currentTimeMillis() - StartTime);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

}
