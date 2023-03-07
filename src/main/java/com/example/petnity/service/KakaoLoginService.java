package com.example.petnity.service;

import com.example.petnity.data.dto.KakaoDto;
import com.example.petnity.data.dto.UserDto;

import java.io.IOException;

public interface KakaoLoginService {

    KakaoDto.TokenInfo getKakaoTokenInfo(String code) throws IOException;

    UserDto.Info getUserInfo(String access_token) throws IOException;
    String getUserInfoByOIDC(String accessToken);

    String getAgreementInfo(String access_token);

    void kakaoLogout(String access_token);
}
