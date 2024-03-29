package com.example.petnity.service.impl;

import com.example.petnity.data.dao.PetDao;
import com.example.petnity.data.dao.UserDao;
import com.example.petnity.data.dto.PetDto;
import com.example.petnity.data.entity.PetEntity;
import com.example.petnity.data.entity.UserEntity;
import com.example.petnity.service.PetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class PetServiceImpl implements PetService {

    private final Logger LOGGER = LoggerFactory.getLogger(PetServiceImpl.class);

    private final PetDao petDao;
    private final UserDao userDao;


    public PetServiceImpl(PetDao petDao, UserDao userDao){
        this.petDao = petDao;
        this.userDao = userDao;
    }

    @Override
    public PetDto.Response savePet(PetDto.Request petDto) {
        LOGGER.info("[PetService] Perform {} of Petnity API.", "savePet");
        LOGGER.info("[PetService] Param :: petDtoRequest = {}", petDto.toString());

        PetEntity petEntity = petDto.toEntity();

        Optional<UserEntity> userEntity = userDao.getUserByUserId(petDto.getOwnerId());
        LOGGER.info("[PetService] Response :: userEntity= {}", userEntity.get().toString());

        petEntity.setOwnerEntity(userEntity.get());

        PetEntity savedPetEntity = petDao.savePet(petEntity);
        LOGGER.info("[PetService] Response :: savedPetEntity= {}", savedPetEntity.toString());

        PetDto.Response savedPetDtoResponse = PetDto.Response.builder()
                .petId(savedPetEntity.getPetId())
                .petName(savedPetEntity.getPetName())
                .petKind(savedPetEntity.getPetKind())
                .petBirthYear(savedPetEntity.getPetBirthYear())
                .petBirthDay(savedPetEntity.getPetBirthDay())
                .petGender(savedPetEntity.getPetGender())
                .build();

        return savedPetDtoResponse;
    }

    @Override
    public PetDto.Response getPetByPetId(Long petId) {
        LOGGER.info("[PetService] Perform {} of Petnity API.", "getPetById");
        LOGGER.info("[PetService] Param :: petId= {}", petId);

        Optional<PetEntity> petEntity = petDao.getPetByPetId(petId);

        LOGGER.info("[PetService] Response :: petEntity= {}", petEntity.toString());

        PetDto.Response petDtoResponse = PetDto.Response.builder()
                .petId(petEntity.get().getPetId())
                .petName(petEntity.get().getPetName())
                .petKind(petEntity.get().getPetKind())
                .petBirthYear(petEntity.get().getPetBirthYear())
                .petBirthDay(petEntity.get().getPetBirthDay())
                .petGender(petEntity.get().getPetGender())
                .build();

        LOGGER.info("[PetService] Response :: petDtoResponse= {}", petDtoResponse.toString());
        return petDtoResponse;
    }

    @Override
    public void deletePetByPetId(Long petId){
        LOGGER.info("[PetService] Perform {} of Petnity API.", "deletePetById");
        LOGGER.info("[PetService] Param :: petId= {}", petId);

        petDao.deletePetByPetId(petId);
    }

}
