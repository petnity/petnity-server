package com.example.petnity.data.dto;

import lombok.*;

public class KakaoDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class TokenInfo {
        private String idToken;
        private String accessToken;
//        private String refreshToken;
//        private Integer expiresIn;
//        private Integer refreshTokenExpiresIn;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class TokenPayload {
        private String userAccount;
        private Integer expiration;
    }
}
