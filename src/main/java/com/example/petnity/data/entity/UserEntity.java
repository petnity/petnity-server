package com.example.petnity.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private boolean userActivated;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<AuthorityEntity> authorities;


    @OneToMany(mappedBy = "ownerEntity")
    private List<PetEntity> petEntityList = new ArrayList<PetEntity>();
    @OneToMany(mappedBy = "user")
    private List<PostEntity> postEntityList = new ArrayList<PostEntity>();
    @OneToMany(mappedBy = "userEntity")
    private List<CommentEntity> commentEntityList = new ArrayList<CommentEntity>();

    public void addPet(PetEntity petEntity){
        this.petEntityList.add(petEntity);
    }

    public void removePet(PetEntity petEntity){
        this.petEntityList.remove(petEntity);
    }

}
