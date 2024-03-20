package com.play.java.bbgeducation.api.auth.links.api;

import com.play.java.bbgeducation.api.auth.UpdatePasswordRequest;
import com.play.java.bbgeducation.api.common.ApiBodyType;


public class ApiUpdatePasswordRequest extends UpdatePasswordRequest {

    public static UpdatePasswordRequest getApiBody(){
        ApiUpdatePasswordRequest request = new ApiUpdatePasswordRequest();
        request.setOldPassword(ApiBodyType.STRING.value);
        request.setNewPassword(ApiBodyType.STRING.value);
        return request;
    }
}
