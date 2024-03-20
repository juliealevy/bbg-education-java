package com.play.java.bbgeducation.api.users.links;

import com.play.java.bbgeducation.api.common.ApiBodyType;
import com.play.java.bbgeducation.api.users.UpdateUserRequest;

public class ApiUpdateUserRequest extends UpdateUserRequest {

    public static UpdateUserRequest getApiBody(){
        return UpdateUserRequest.builder()
                .firstName(ApiBodyType.STRING.value)
                .lastName(ApiBodyType.STRING.value)
                .build();
    }
}
