package com.example.petnity.data.dao;

import com.example.petnity.data.entity.UserEntity;

import java.util.Optional;

public interface UserDao {
    UserEntity saveUser(UserEntity userEntity);
    void deleteUserById(Long userId);
    void deleteUserByEmail(String userEmail);
    Optional<UserEntity> getUserByUserId(Long userId);
    Optional<UserEntity> getUserByEmail(String userEmail);
    Optional<UserEntity> getUserByUserAccount(String userAccount);
}
