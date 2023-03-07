package com.example.petnity.service.impl;

import com.example.petnity.data.dao.PetDao;
import com.example.petnity.data.dao.UserDao;
import com.example.petnity.data.dto.PetDto;
import com.example.petnity.data.dto.UserDto;
import com.example.petnity.data.entity.PetEntity;
import com.example.petnity.data.entity.UserEntity;
import com.example.petnity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final PetDao petDao;

    public UserServiceImpl(UserDao userDao, PetDao petDao){
        this.userDao = userDao;
        this.petDao = petDao;
    }

    @Override
    public UserDto.Response saveUser(UserDto.Request userDto) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "saveUser - request");
        LOGGER.info("[UserService] Param :: userDtoInfo = {}", userDto.toString());

        UserEntity userEntity = userDto.toEntity();
        LOGGER.info("[UserService] UserToEntity :: userEntity = {}", userEntity.toString());

        UserEntity savedUserEntity = userDao.saveUser(userEntity);
        LOGGER.info("[UserService] UserDao :: savedUserEntity = {}", savedUserEntity.toString());

        UserDto.Response savedUserDtoResponse = UserDto.Response.builder()
                .userId(savedUserEntity.getUserId())
                .userAccount(savedUserEntity.getUserAccount())
                .userEmail(savedUserEntity.getUserEmail())
                .userNickname(savedUserEntity.getUserNickname())
                .userName(savedUserEntity.getUserName())
                .userThumbnailImageUrl(savedUserEntity.getUserThumbnailImageUrl())
                .userProfileImageUrl(savedUserEntity.getUserProfileImageUrl())
                .userGender(savedUserEntity.getUserGender())
                .userBirthYear(savedUserEntity.getUserBirthYear())
                .userBirthDay(savedUserEntity.getUserBirthDay())
                .build();
        LOGGER.info("[UserService] User :: savedUserDtoInfo = {}", savedUserDtoResponse.toString());

        return savedUserDtoResponse;
    }

    @Override
    public UserDto.Response saveUser(UserDto.Info userDto) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "saveUser - info");
        LOGGER.info("[UserService] Param :: userDtoInfo = {}", userDto.toString());

        UserEntity userEntity = userDto.toEntity();
        LOGGER.info("[UserService] UserToEntity :: userEntity = {}", userEntity.toString());

        if (userDto.getPetDtoResponseList() != null) {
            List<PetDto.Response> petDtoResponseList = userDto.getPetDtoResponseList();

            for (PetDto.Response petDtoResponse : petDtoResponseList) {
                PetEntity petEntity = PetEntity.builder()
                        .petId(petDtoResponse.getPetId())
                        .petName(petDtoResponse.getPetName())
                        .petKind(petDtoResponse.getPetKind())
                        .petGender(petDtoResponse.getPetGender())
                        .petBirthYear(petDtoResponse.getPetBirthYear())
                        .petBirthDay(petDtoResponse.getPetBirthDay())
                        .build();
                petEntity.setOwnerEntity(userEntity);
            }
            LOGGER.info("[UserService] User, add pet list :: userEntity = {}", userEntity);
        }
        UserEntity savedUserEntity = userDao.saveUser(userEntity);
        LOGGER.info("[UserService] User, save entity :: savedUserEntity = {}", savedUserEntity.toString());

        UserDto.Response savedUserDtoResponse = UserDto.Response.builder()
                .userId(savedUserEntity.getUserId())
                .userAccount(savedUserEntity.getUserAccount())
                .userEmail(savedUserEntity.getUserEmail())
                .userNickname(savedUserEntity.getUserNickname())
                .userName(savedUserEntity.getUserName())
                .userThumbnailImageUrl(savedUserEntity.getUserThumbnailImageUrl())
                .userProfileImageUrl(savedUserEntity.getUserProfileImageUrl())
                .userGender(savedUserEntity.getUserGender())
                .userBirthYear(savedUserEntity.getUserBirthYear())
                .userBirthDay(savedUserEntity.getUserBirthDay())
                .build();
        LOGGER.info("[UserService] User, saved dto :: savedUserDtoInfo = {}", savedUserDtoResponse.toString());

        return savedUserDtoResponse;
    }

    @Override
    public UserDto.Response getUser(Optional<UserEntity> userEntity) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUser");
        LOGGER.info("[UserService] Param :: userEntity = {}", userEntity);

        List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();

        List<PetEntity> petEntityList = petDao.getAllPetByOwnerId(userEntity.get().getUserId());
        LOGGER.info("[UserService] PetDao Response :: petEntityList = {}", petEntityList);

        for(PetEntity petEntity : petEntityList){
            petEntity.setOwnerEntity(userEntity.get());
            PetDto.Response petDto = PetDto.Response.builder()
                    .petId(petEntity.getPetId())
                    .petName(petEntity.getPetName())
                    .petKind(petEntity.getPetKind())
                    .petGender(petEntity.getPetGender())
                    .petBirthYear(petEntity.getPetBirthYear())
                    .petBirthDay(petEntity.getPetBirthDay())
                    .build();
            petDtoResponseList.add(petDto);
            LOGGER.info("[UserService] Response :: petDto = {}", petDto.toString());
        }
        LOGGER.info("[UserService] Response :: petDtoResponseList = {}", petDtoResponseList.toString());

        UserDto.Response userDtoResponse = UserDto.Response.builder()
                .userId(userEntity.get().getUserId())
                .userAccount(userEntity.get().getUserAccount())
                .userEmail(userEntity.get().getUserEmail())
                .userNickname(userEntity.get().getUserNickname())
                .userName(userEntity.get().getUserName())
                .userGender(userEntity.get().getUserGender())
                .userBirthYear(userEntity.get().getUserBirthYear())
                .userBirthDay(userEntity.get().getUserBirthDay())
                .petDtoResponseList(petDtoResponseList)
                .build();
        LOGGER.info("[UserService] Response :: userDtoInfo = {}", userDtoResponse.toString());

        return userDtoResponse;
    }

    @Override
    public UserDto.Info getUserInfo(Optional<UserEntity> userEntity) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserInfo");
        LOGGER.info("[UserService] Param :: userEntity = {}", userEntity);

        List<PetDto.Response> petDtoResponseList = new ArrayList<PetDto.Response>();

        List<PetEntity> petEntityList = petDao.getAllPetByOwnerId(userEntity.get().getUserId());
        LOGGER.info("[UserService] PetDao Response :: petEntityList = {}", petEntityList);

        for(PetEntity petEntity : petEntityList){
            petEntity.setOwnerEntity(userEntity.get());
            PetDto.Response petDto = PetDto.Response.builder()
                    .petId(petEntity.getPetId())
                    .petName(petEntity.getPetName())
                    .petKind(petEntity.getPetKind())
                    .petGender(petEntity.getPetGender())
                    .petBirthYear(petEntity.getPetBirthYear())
                    .petBirthDay(petEntity.getPetBirthDay())
                    .build();
            petDtoResponseList.add(petDto);
            LOGGER.info("[UserService] Response :: petDto = {}", petDto.toString());
        }
        LOGGER.info("[UserService] Response :: petDtoResponseList = {}", petDtoResponseList.toString());

        UserDto.Info userDtoInfo = UserDto.Info.builder()
                .userId(userEntity.get().getUserId())
                .userAccount(userEntity.get().getUserAccount())
                .userEmail(userEntity.get().getUserEmail())
                .userPassword(userEntity.get().getUserPassword())
                .userNickname(userEntity.get().getUserNickname())
                .userName(userEntity.get().getUserName())
                .userGender(userEntity.get().getUserGender())
                .userBirthYear(userEntity.get().getUserBirthYear())
                .userBirthDay(userEntity.get().getUserBirthDay())
                .petDtoResponseList(petDtoResponseList)
                .build();
        LOGGER.info("[UserService] Response :: userDtoInfo = {}", userDtoInfo.toString());

        return userDtoInfo;
    }

    @Override
    public UserDto.Response getUserByUserId(Long userId) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserById");
        LOGGER.info("[UserService] Param :: userId = {}", userId);

        Optional<UserEntity> userEntity = userDao.getUserByUserId(userId);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        UserDto.Response userResponse = getUser(userEntity);
        return userResponse;
    }

    @Override
    public UserDto.Response getUserByUserEmail(String userEmail) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserByEmail");
        LOGGER.info("[UserService] Param :: userEmail = {}", userEmail);

        Optional<UserEntity> userEntity = userDao.getUserByEmail(userEmail);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        UserDto.Response userResponse = getUser(userEntity);
        return userResponse;
    }

    @Override
    public UserDto.Info getUserInfoByUserEmail(String userEmail) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserInfoByEmail");
        LOGGER.info("[UserService] Param :: userEmail = {}", userEmail);

        Optional<UserEntity> userEntity = userDao.getUserByEmail(userEmail);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        UserDto.Info userInfo = getUserInfo(userEntity);
        return userInfo;
    }

    @Override
    public UserDto.Response getUserByUserAccount(String userAccount) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserByKakaoAccount");
        LOGGER.info("[UserService] Param :: userAccount = {}", userAccount);

        Optional<UserEntity> userEntity = userDao.getUserByUserAccount(userAccount);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        UserDto.Response userResponse = getUser(userEntity);
        return userResponse;
    }

    @Override
    public UserDto.Info getUserInfoByUserAccount(String userAccount) {
        LOGGER.info("[UserService] Perform {} of Petnity API.", "getUserByKakaoAccount");
        LOGGER.info("[UserService] Param :: userAccount = {}", userAccount);

        Optional<UserEntity> userEntity = userDao.getUserByUserAccount(userAccount);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        UserDto.Info userInfo = getUserInfo(userEntity);
        return userInfo;
    }

    @Override
    public void deleteUserByUserId(Long userId) {
        LOGGER.info("[UserService] perform {} of Petnity API.", "deleteUser");

        Optional<UserEntity> userEntity = userDao.getUserByUserId(userId);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        List<PetEntity> petEntityList = petDao.getAllPetByOwnerId(userEntity.get().getUserId());
        LOGGER.info("[UserService] PetDao Response :: petEntityList = {}", petEntityList);

        for(PetEntity petEntity : petEntityList){
            LOGGER.info("[UserService] Response :: petId = {}", petEntity.getPetId());
            petDao.deletePetByPetId(petEntity.getPetId());
        }

        userDao.deleteUserById(userId);

    }

    @Override
    public void deleteUserByEmail(String userEmail) {
        LOGGER.info("[UserService] perform {} of Petnity API.", "deleteUser");

        Optional<UserEntity> userEntity = userDao.getUserByEmail(userEmail);
        LOGGER.info("[UserService] UserDao Response :: userEntity = {}", userEntity.get());

        List<PetEntity> petEntityList = petDao.getAllPetByOwnerId(userEntity.get().getUserId());
        LOGGER.info("[UserService] PetDao Response :: petEntityList = {}", petEntityList);

        for(PetEntity petEntity : petEntityList){
            LOGGER.info("[UserService] Response :: petId = {}", petEntity.getPetId());
            petDao.deletePetByPetId(petEntity.getPetId());
        }

        userDao.deleteUserByEmail(userEmail);
    }
}
