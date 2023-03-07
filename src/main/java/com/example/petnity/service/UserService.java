package com.example.petnity.service;

import com.example.petnity.data.dto.UserDto;
import com.example.petnity.data.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    UserDto.Response saveUser(UserDto.Request userDto);

    UserDto.Response saveUser(UserDto.Info userDto);

    UserDto.Response getUser(Optional<UserEntity> userEntity);
    UserDto.Info getUserInfo(Optional<UserEntity> userEntity);

    UserDto.Response getUserByUserId(Long userId);
    UserDto.Response getUserByUserEmail(String userEmail);
    UserDto.Info getUserInfoByUserEmail(String userEmail);

    UserDto.Response getUserByUserAccount(String userAccount);
    UserDto.Info getUserInfoByUserAccount(String userAccount);

    void deleteUserByUserId(Long userId);

    void deleteUserByEmail(String userEmail);
}
