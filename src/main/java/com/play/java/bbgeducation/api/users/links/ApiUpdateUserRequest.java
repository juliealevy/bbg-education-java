package com.play.java.bbgeducation.api.users.links;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;

public class ApiUpdateUserRequest extends UpdateUserRequest {

    private static final String stringType = String.class.getSimpleName().toLowerCase();

    public static UpdateUserRequest getApiBody(){
        return UpdateUserRequest.builder()
                .firstName(stringType)
                .lastName(stringType)
                .email(stringType)
                .build();
    }
}
