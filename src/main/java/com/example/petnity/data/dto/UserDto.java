package com.example.petnity.data.dto;

import com.example.petnity.data.entity.PetEntity;
import com.example.petnity.data.entity.UserEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class UserDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Info {
        private Long userId;
        private String accessToken;
        private String refreshToken;
        private String userAccount;
        private String userEmail;
        private String userPassword;
        private String userNickname;
        private String userName;
        private String userThumbnailImageUrl;
        private String userProfileImageUrl;
        private String userGender;
        private String userBirthYear;
        private String userBirthDay;
        private List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();

        public UserEntity toEntity(){
            return UserEntity.builder()
                    .userId(userId)
                    .userAccount(userAccount)
                    .userEmail(userEmail)
                    .userPassword(userPassword)
                    .userNickname(userNickname)
                    .userName(userName)
                    .userPassword(userPassword)
                    .userThumbnailImageUrl(userThumbnailImageUrl)
                    .userProfileImageUrl(userProfileImageUrl)
                    .userGender(userGender)
                    .userBirthYear(userBirthYear)
                    .userBirthDay(userBirthDay)
                    .petEntityList(new ArrayList<PetEntity>())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Login {
        private String userAccount;
        private String userPassword;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class TokenInfo {
        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Request{
        private Long userId;
        private String accessToken;
        private String refreshToken;
        private String userAccount;
        private String userEmail;
        private String userNickname;
        private String userName;
        private String userThumbnailImageUrl;
        private String userProfileImageUrl;
        private String userGender;
        private String userBirthYear;
        private String userBirthDay;


        public UserEntity toEntity(){
            return UserEntity.builder()
                    .userId(userId)
                    .userAccount(userAccount)
                    .userEmail(userEmail)
                    .userNickname(userNickname)
                    .userName(userName)
                    .userThumbnailImageUrl(userThumbnailImageUrl)
                    .userProfileImageUrl(userProfileImageUrl)
                    .userGender(userGender)
                    .userBirthYear(userBirthYear)
                    .userBirthDay(userBirthDay)
                    .build();
        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Response{
        private Long userId;
        private String accessToken;
        private String userAccount;
        private String userEmail;
        private String userNickname;
        private String userName;
        private String userGender;
        private String userThumbnailImageUrl;
        private String userProfileImageUrl;
        private String userBirthYear;
        private String userBirthDay;
        private List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class PetList {
        private String accessToken;
        private List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();
    }

}
