package com.example.project1.Credentials;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long tokenid;

    @Column
    private String confirmationtoken;

    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userid")
    private UserEntity userEntity;

    public TokenEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        confirmationtoken = UUID.randomUUID().toString();
    }

    public TokenEntity() {

    }

    public Long getTokenid() {

        return tokenid;
    }

    public void setTokenid(Long tokenid) {

        this.tokenid = tokenid;
    }

    public String getConfirmationtoken() {

        return confirmationtoken;
    }

    public void setConfirmationtoken(String confirmationtoken) {

        this.confirmationtoken = confirmationtoken;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
