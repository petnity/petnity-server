package com.example.petnity.data.entity;

import com.example.petnity.data.dto.PetDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "user")
public class UserEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
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
    @OneToMany(mappedBy = "ownerEntity")
    private List<PetEntity> petEntityList = new ArrayList<PetEntity>();

    public void addPet(PetEntity petEntity){
        this.petEntityList.add(petEntity);
    }

    public void removePet(PetEntity petEntity){
        this.petEntityList.remove(petEntity);
    }

}
