package com.example.petnity.controller;

import com.example.petnity.data.dto.PetDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.security.JwtManager;
import com.example.petnity.service.PetService;
import com.example.petnity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/pet-api")
public class PetController {

    private final Logger LOGGER = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;
    private final UserService userService;
    private final JwtManager jwtManager;


    public PetController (PetService petService,
                          UserService userService,
                          JwtManager jwtManager) {
        this.petService = petService;
        this.userService = userService;
        this.jwtManager = jwtManager;
    }
    

    // TODO PetController how to response new access Token
    @PostMapping("/pet/create")
    public ResponseEntity<PetDto.Response> createPet(@Valid @RequestBody PetDto.Request petDto){
        LOGGER.info("[PetController] Param :: petRequest", petDto.toString());

        UserDto.TokenInfo userTokenInfoDto = UserDto.TokenInfo.builder()
                .accessToken(petDto.getAccessToken())
                .refreshToken(petDto.getRefreshToken())
                .build();
        
        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);
        LOGGER.info("[PetController] Token :: checkedAccessToken = {}", checkedAccessToken);

        if (checkedAccessToken.equals("")) {
            LOGGER.info("[PetController] Exception :: Token Expired");

            PetDto.Response badResponse = PetDto.Response.builder().build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[PetController] Token :: Valid Token");

        Long userId = jwtManager.getUserIdFromToken(checkedAccessToken);

        // Token user and ownerId must be same
        if (petDto.getOwnerId() != userId) {
            LOGGER.warn("[PetController] Exception :: Unmatched User");

            PetDto.Response badResponse = PetDto.Response.builder().build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badResponse);
        }

        PetDto.Response response = petService.savePet(petDto);
        response.setAccessToken(checkedAccessToken);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    

    @PostMapping("/pet/udpate")
    public ResponseEntity<PetDto.Response> updatePet(@Valid @RequestBody PetDto.Request petDto){
        LOGGER.info("[PetController] Param :: petRequest", petDto.toString());

        UserDto.TokenInfo userTokenInfoDto = UserDto.TokenInfo.builder()
                .accessToken(petDto.getAccessToken())
                .refreshToken(petDto.getRefreshToken())
                .build();

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);
        LOGGER.info("[PetController] Token :: checkedAccessToken = {}", checkedAccessToken);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[PetController] Exception :: Token Expired");

            PetDto.Response badResponse = PetDto.Response.builder().build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[PetController] Token :: Valid Token");

        PetDto.Response response = petService.savePet(petDto);
        response.setAccessToken(checkedAccessToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/pet/")
    public ResponseEntity<PetDto.Response> getPet(@RequestBody PetDto.Request petDto) {

        LOGGER.info("[PetController] Param :: petRequest", petDto.toString());

        UserDto.TokenInfo userTokenInfoDto = UserDto.TokenInfo.builder()
                .accessToken(petDto.getAccessToken())
                .refreshToken(petDto.getRefreshToken())
                .build();

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);
        LOGGER.info("[PetController] Token :: checkedAccessToken = {}", checkedAccessToken);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[PetController] Exception :: Token Expired");

            PetDto.Response badResponse = PetDto.Response.builder().build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[PetController] Token :: Valid Token");
        
        Long petId = petDto.getPetId();
        PetDto.Response response = petService.getPetByPetId(petId);

        if (response.getPetId() == null) {
            LOGGER.warn("[PetController] Exception :: There is no {}", petId);

            PetDto.Response badResponse = PetDto.Response.builder().build();

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(badResponse);
        }
        response.setAccessToken(checkedAccessToken);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    
    @DeleteMapping("/pet/delete")
    public ResponseEntity<UserDto.TokenInfo> deletePet(@RequestBody PetDto.Request petDto) {
        LOGGER.info("[PetController] Param :: petRequest", petDto.toString());
        
        UserDto.TokenInfo userTokenInfoDto = UserDto.TokenInfo.builder()
                .accessToken(petDto.getAccessToken())
                .refreshToken(petDto.getRefreshToken())
                .build();

        String checkedAccessToken = jwtManager.tokenValidationChecker(userTokenInfoDto);
        LOGGER.info("[PetController] Token :: checkedAccessToken = {}", checkedAccessToken);

        if (checkedAccessToken.equals("")) {
            LOGGER.warn("[PetController] Exception :: Token Expired");

            UserDto.TokenInfo badResponse = UserDto.TokenInfo.builder().build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(badResponse);
        }
        LOGGER.info("[PetController] Token :: Valid Token");
        
        Long petId = petDto.getPetId();

        petService.deletePetByPetId(petId);

        String message = petId + " is deleted";

        UserDto.TokenInfo response = UserDto.TokenInfo.builder()
                .accessToken(checkedAccessToken)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//    @PostMapping("/pet/")
//    public ResponseEntity<PetDto.Response> getPet(@PathVariable Long petId){
//        LOGGER.info("[PetController] perform {} of Petnity API.", "getPet");
//
//        PetDto.Response response = petService.getPetByPetId(petId);
//
//        if (response.getPetId() == null) {
//            LOGGER.error("[PetController] failed Response :: There is no {}", petId);
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        LOGGER.info("[PetController] Response :: petDtoInfo = {}", response.toString());
//
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }
//    
//
//    
//    @DeleteMapping("/pet/{petId}")
//    public ResponseEntity<String> deletePet(@PathVariable Long petId){
//        LOGGER.info("[PetController] perform {} of Petnity API.", "deleteUser");
//
//        long StartTime = System.currentTimeMillis();
//        LOGGER.info("[PetController] Response :: petId = {}", petId);
//
//        petService.deletePetByPetId(petId);
//
//        String response = petId + " is deleted";
//
//        LOGGER.info("[PetController] Response :: response = {}, Response Time = {}ms", response, System.currentTimeMillis() - StartTime);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

}
