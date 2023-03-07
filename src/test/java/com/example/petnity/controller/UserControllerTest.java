package com.example.petnity.controller;

import com.example.petnity.data.dto.PetDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.data.entity.PetEntity;
import com.example.petnity.data.entity.UserEntity;
import com.example.petnity.service.UserService;
import com.example.petnity.service.impl.UserServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;

    // http://localhost:8080/api/v1/user-api/user/{userEmail}
    @Test
    @DisplayName("[TEST] Get user data by user email")
    void  getUserByEmailTest() throws Exception{
//        String userEmail = "sample@example.com";
//
//        PetDto.Response petResponse = PetDto.Response.builder()
//                .petName("BBOBBI")
//                .petKind("Dog")
//                .petGender("male")
//                .petBirthYear("2020")
//                .petBirthDay("0101")
//                .build();
//
//        List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();
//        petDtoResponseList.add(petResponse);
//
//        given(userService.getUserByEmail(userEmail)).willReturn(
//                new UserDto.Info(1L, userEmail, "pwd", "nickname",
//                        "name", "male", "1997", "0101", petDtoResponseList));
//
//        mockMvc.perform(
//                get("/api/v1/user-api/user/" + userEmail))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userEmail").exists())
//                .andExpect(jsonPath("$.userPassword").exists())
//                .andExpect(jsonPath("$.userNickname").exists())
//                .andExpect(jsonPath("$.userName").exists())
//                .andExpect(jsonPath("$.userGender").exists())
//                .andExpect(jsonPath("$.userBirthYear").exists())
//                .andExpect(jsonPath("$.userBirthDay").exists())
//                .andExpect(jsonPath("$.petDtoInfoList").exists())
//                .andDo(print());
//
//        verify(userService).getUserByEmail(userEmail);
    }

    @Test
    @DisplayName("[TEST] Create user data")
    void createUserTest() throws Exception {
//
//        PetDto.Response petResponse = PetDto.Response.builder()
//                .petName("BBOBBI")
//                .petKind("Dog")
//                .petGender("male")
//                .petBirthYear("2020")
//                .petBirthDay("0101")
//                .build();
//
//        List<PetDto.Response> petList = new ArrayList<PetDto.Response>();
//        petList.add(petResponse);
//
//        UserDto.Info userInfo = UserDto.Info.builder()
//                .userEmail("smaple@example.com")
//                .userPassword("pwd")
//                .userNickname("nickname")
//                .userName("name")
//                .userGender("male")
//                .userBirthYear("1997")
//                .userBirthDay("0101")
//                .petDtoResponseList(petList)
//                .build();
//
//        UserEntity userEntity = UserEntity.builder()
//                .userEmail(userInfo.getUserEmail())
//                .userPassword(userInfo.getUserPassword())
//                .userNickname(userInfo.getUserNickname())
//                .userName(userInfo.getUserName())
//                .userGender(userInfo.getUserGender())
//                .userBirthYear(userInfo.getUserBirthYear())
//                .userBirthDay(userInfo.getUserBirthDay())
//                .petEntityList(new ArrayList<>())
//                .build();
//
//        List<PetDto.Response> petDtoResponses = userInfo.getPetDtoResponseList();
//
//        for(PetDto.Response petDtoResponse : petDtoResponses){
//            PetEntity petEntity = PetEntity.builder()
//                    .petName(petDtoResponse.getPetName())
//                    .petKind(petDtoResponse.getPetKind())
//                    .petBirthYear(petDtoResponse.getPetBirthYear())
//                    .petBirthDay(petDtoResponse.getPetBirthDay())
//                    .build();
//            petEntity.setOwnerEntity(userEntity);
//        }
//
//        given(userService.saveUser(userInfo)).willReturn(
//                        new UserDto.Response(1L, "sample@example.com", "pwd", "nickname",
//                                "name", "male", "1997", "0101", petDtoResponses));
//
//        Gson gson = new Gson();
//        String content = gson.toJson(userInfo);
//
//        mockMvc.perform(
//                post("/api/v1/user-api/user/signup")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.userEmail").exists())
//                .andExpect(jsonPath("$.userPassword").exists())
//                .andExpect(jsonPath("$.userNickname").exists())
//                .andExpect(jsonPath("$.userName").exists())
//                .andExpect(jsonPath("$.userGender").exists())
//                .andExpect(jsonPath("$.userBirthYear").exists())
//                .andExpect(jsonPath("$.userBirthDay").exists())
//                .andDo(print());
//
//        verify(userService).saveUser(userInfo);
    }

}
