package com.play.java.bbgeducation.api.users.links;

import com.play.java.bbgeducation.api.common.ApiBodyType;
import com.play.java.bbgeducation.api.users.GetUserByEmailRequest;

public class ApiGetUserByEmailRequest extends GetUserByEmailRequest {

    public static GetUserByEmailRequest getApiBody(){
        GetUserByEmailRequest request = GetUserByEmailRequest.builder()
                .email(ApiBodyType.STRING.value)
                .build();
        return request;
    }
}
