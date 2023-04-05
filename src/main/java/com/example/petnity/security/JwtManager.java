package com.example.petnity.security;

import com.example.petnity.data.dto.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtManager {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtManager.class);
    private final String securityKey = "petnitySecurityKey";
    private final Long accessTokenExpiredTime = 1000 * 60L * 60L * 12L;
    private final Long refreshTokenExpiredTime = accessTokenExpiredTime * 2L * 10;

    public UserDto.TokenInfo createTokenInfo(UserDto.Info userInfoDto) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getTokenInfo");

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + accessTokenExpiredTime);
        Date refreshTokenExpiresIn = new Date(now.getTime() + refreshTokenExpiredTime);

        String accessToken = Jwts.builder()
                .setSubject(userInfoDto.getUserAccount())
                .setHeader(createHeader())
                .setClaims(createClaims(userInfoDto))
                .setExpiration(expiresIn)
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();

        LOGGER.info("[JwtManager] Token :: accessToken={}", accessToken);
        LOGGER.info("[JwtManager] Token Expiration :: expiresIn={}", expiresIn);

        String refreshToken = Jwts.builder()
                .setSubject(userInfoDto.getUserAccount())
                .setHeader(createHeader())
                .setClaims(createClaims(userInfoDto))
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();

        LOGGER.info("[JwtManager] Token :: refreshToken={}", refreshToken);
        LOGGER.info("[JwtManager] Token Expiration :: refreshTokenExpiresIn={}", refreshTokenExpiresIn);

        UserDto.TokenInfo tokenInfo = new UserDto.TokenInfo(accessToken, refreshToken);

        return tokenInfo;
    }


    public String createAccessToken(UserDto.Info userInfoDto) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getAccessToken");

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(userInfoDto.getUserAccount())
                .setHeader(createHeader())
                .setClaims(createClaims(userInfoDto))
                .setExpiration(new Date(now.getTime() + accessTokenExpiredTime))
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();

        LOGGER.info("[JwtManager] Token :: accessToken={}", accessToken);

        return accessToken;
    }


    public String tokenValidationChecker(UserDto.TokenInfo userTokenInfoDto) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "tokenValidationChecker");

        String accessToken = userTokenInfoDto.getAccessToken();
        LOGGER.info("[JwtManager] Param :: accessToken={}", accessToken);
        Date accessTokenExpiration = getExpiration(accessToken);
        LOGGER.info("[JwtManager] Response :: accessTokenExpiration={}", accessTokenExpiration);

        String refreshToken = userTokenInfoDto.getRefreshToken();
        LOGGER.info("[JwtManager] Param :: refreshToken={}", refreshToken);
        Date refreshTokenExpiration = getExpiration(refreshToken);
        LOGGER.info("[JwtManager] Response :: refreshTokenExpiration={}", refreshTokenExpiration);

        if (isTokenExpired(accessToken)) {
            LOGGER.info("[JwtManager] Exception :: Access Token Expired. accessTokenExpiration={}", accessTokenExpiration);
            if (isTokenExpired(refreshToken)) {
                LOGGER.info("[JwtManager] Exception :: Refresh Token Expired. refreshTokenExpiration={}", refreshTokenExpiration);
                accessToken = "";

            }
            else {
                accessToken = getNewAccessToken(accessToken);
                LOGGER.info("[JwtManager] Response :: New Access Token = {}", accessToken);
            }
        }

        return accessToken;
    }


    public Boolean isTokenExpired(String token){
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "isTokenExpired");

        Claims claims = getClaims(token);
        return claims.getExpiration().before(new Date());
    }


    public Date getExpiration(String token){
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getExpirtaion");
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }


    private Map<String, Object> createHeader() {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "createHeader");

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());

        LOGGER.info("[JwtManager] Token :: header={}", header);

        return header;
    }


    private Map<String, Object> createClaims(UserDto.Info userInfoDto) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "createClaims");

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userInfoDto.getUserId());
        claims.put("userAccount", userInfoDto.getUserAccount());
        claims.put("userPassword", userInfoDto.getUserPassword());

        LOGGER.info("[JwtManager] Token :: claims={}", claims);

        return claims;
    }


    private Claims getClaims(String token) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getClaims");

        Claims claims = Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
        LOGGER.info("[JwtManager] Response :: claims = {}", claims.toString());

        return claims;
    }


    public Long getUserIdFromToken(String token) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getUserIdFromToken");

        Long userId = getClaims(token).get("userId", Long.class);
        LOGGER.info("[JwtManager] User :: userId = {} ", userId);

        return userId;
    }


    public String getUserAccountFromToken(String token) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getUserAccountFromToken");

        String userAccount = getClaims(token).get("userAccount", String.class);
        LOGGER.info("[JwtManager] User :: userAccount = {} ", userAccount);

        return userAccount;
    }


    private String getUserPasswordFromToken(String token) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getUserPasswordFromToken");

        String userPassword = getClaims(token).get("userPassword", String.class);
        LOGGER.info("[JwtManager] User :: userPassword = {} ", userPassword);

        return userPassword;
    }

    public String getNewAccessToken(String oldAccessToken) {
        LOGGER.info("[JwtManager] Perform {} of Petnity API.", "getNewAccessToken");

        Long userId = getUserIdFromToken(oldAccessToken);
        String userAccount = getUserAccountFromToken(oldAccessToken);
        String userPassword = getUserPasswordFromToken(oldAccessToken);

        UserDto.Info userInfoDto = UserDto.Info.builder()
                .userId(userId)
                .userAccount(userAccount)
                .userPassword(userPassword)
                .build();

        String newAccessToken = createAccessToken(userInfoDto);
        LOGGER.info("[JwtManager] Token :: newAccessToken = {}", newAccessToken);

        return newAccessToken;
    }
}
