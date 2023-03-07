package com.example.petnity.service;

import com.example.petnity.data.dto.KakaoDto;
import com.example.petnity.data.dto.UserDto;

import java.io.IOException;

public interface KakaoLoginWebService {

    KakaoDto.TokenInfo getKakaoTokenInfo(String code) throws IOException;

    UserDto.Info getUserInfo(String accessToken) throws IOException;

    String getUserInfoByOIDC(String accessToken);

    KakaoDto.TokenPayload payloadParser(String payload);

    KakaoDto.TokenPayload idTokenParser(String idToken);

    String getAgreementInfo(String accessToken);

    void kakaoLogout(String accessToken);
}
