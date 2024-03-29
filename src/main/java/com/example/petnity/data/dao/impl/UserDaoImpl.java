package com.example.petnity.data.dao.impl;

import com.example.petnity.data.dao.UserDao;
import com.example.petnity.data.entity.UserEntity;
import com.example.petnity.data.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDaoImpl implements UserDao {

    UserRepository userRepository;

    @Autowired
    public UserDaoImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity saveUser(UserEntity userEntity) {
        UserEntity saved = userRepository.save(userEntity);
        return saved;
    }

    @Override
    public Optional<UserEntity> getUserByUserId(Long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity;
    }


    @Override
    public Optional<UserEntity> getUserByEmail(String userEmail) {
        Optional<UserEntity> userEntity = Optional.of(userRepository.findByUserEmail(userEmail).orElse(new UserEntity()));
        return userEntity;
    }


    @Override
    public Optional<UserEntity> getUserByUserAccount(String userAccount) {
        Optional<UserEntity> userEntity = Optional.of(userRepository.findByUserAccount(userAccount).orElse(new UserEntity()));
        return userEntity;
    }


    @Override
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }


    @Override
    public void deleteUserByEmail(String userEmail) {
        userRepository.deleteByUserEmail(userEmail);
    }

}
