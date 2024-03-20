package com.play.java.bbgeducation.api.auth.links.api;

import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.common.ApiBodyType;

public class ApiLoginRequest extends LoginRequest {
    public static LoginRequest getApiBody(){
        return LoginRequest.builder()
                .email(ApiBodyType.STRING.value)
                .password(ApiBodyType.STRING.value)
                .build();
    }
}
