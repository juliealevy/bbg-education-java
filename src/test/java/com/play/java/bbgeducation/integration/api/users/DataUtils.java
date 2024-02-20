package com.play.java.bbgeducation.integration.api.users;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;

public class DataUtils {

    public static UserEntity buildUserEntity1(){
        return UserEntity.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .password("123456")
                .isAdmin(false)
                .build();
    }

    public static UserEntity buildUserEntity2(){
        return UserEntity.builder()
                .firstName("Mary")
                .lastName("Poppins")
                .email("mary@flymail.com")
                .password("123456")
                .isAdmin(false)
                .build();
    }

    public static UpdateUserRequest buildUpdateUserRequest(){
        return UpdateUserRequest.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .password("123456")
                .build();
    }

}
