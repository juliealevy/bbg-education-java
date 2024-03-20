package com.play.java.bbgeducation.api.auth.links.api;

import com.play.java.bbgeducation.api.auth.UpdateUserNameRequest;
import com.play.java.bbgeducation.api.common.ApiBodyType;

public class ApiUpdateUserNameRequest extends UpdateUserNameRequest {

    public static UpdateUserNameRequest getApiBody(){
        ApiUpdateUserNameRequest request = new ApiUpdateUserNameRequest();
        request.setEmail(ApiBodyType.STRING.value);

        return request;
    }
}
