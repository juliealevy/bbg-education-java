package com.play.java.bbgeducation.integration.auth;

import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;

public final class DataUtils {

    public static LoginRequest buildLoginRequest1(){
        return LoginRequest.builder()
                .email("burt@flymail.com")
                .password("123456")
                .build();
    }

    public static LoginRequest buildLoginRequest2(){
        return LoginRequest.builder()
                .email("mary@flymail.com")
                .password("123456")
                .build();
    }

    public static RegisterRequest buildRegisterRequest1(){
        return RegisterRequest.builder()
                .email("mary@flymail.com")
                .password("123456")
                .firstName("Mary")
                .lastName("Poppins")
                .build();
    }

    public static RegisterRequest buildRegisterRequest2(){
        return RegisterRequest.builder()
                .email("burt@flymail.com")
                .password("123456")
                .firstName("Burt")
                .lastName("Sweep")
                .build();
    }
}
