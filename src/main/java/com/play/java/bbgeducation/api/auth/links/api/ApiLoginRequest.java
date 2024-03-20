package com.play.java.bbgeducation.api.auth.links.api;

import com.play.java.bbgeducation.api.auth.LoginRequest;

public class ApiLoginRequest extends LoginRequest {
    public static LoginRequest getApiBody(){
        return LoginRequest.builder()
                .email(String.class.getSimpleName().toLowerCase())
                .password(String.class.getSimpleName().toLowerCase())
                .build();
    }
}
