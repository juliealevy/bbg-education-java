package com.play.java.bbgeducation.integration.users;

import com.play.java.bbgeducation.api.users.UserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;

public class DataUtils {

    public static UserEntity buildUserEntity1(){
        return UserEntity.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .password("123456")
                .build();
    }

    public static UserEntity buildUserEntity2(){
        return UserEntity.builder()
                .firstName("Mary")
                .lastName("Poppins")
                .email("mary@flymail.com")
                .password("123456")
                .build();
    }

    public static UserRequest buildUserRequest1(){
        return UserRequest.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .password("123456")
                .build();
    }

    public static UserRequest buildUserRequest2(){
        return UserRequest.builder()
                .firstName("Mary")
                .lastName("Poppins")
                .email("mary@flymail.com")
                .password("123456")
                .build();
    }
}
