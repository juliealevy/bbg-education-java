package com.play.java.bbgeducation.api.auth.links.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.api.common.ApiBodyType;

public class ApiRegisterRequest  extends RegisterRequest {

    @JsonProperty("isAdmin")
    private String isAdminString = ApiBodyType.BOOLEAN.value;
    public static RegisterRequest getApiBody(){

        ApiRegisterRequest request = new ApiRegisterRequest();
        request.setEmail(ApiBodyType.STRING.value);
        request.setPassword(ApiBodyType.STRING.value);
        request.setFirstName(ApiBodyType.STRING.value);
        request.setLastName(ApiBodyType.STRING.value);
        request.setIsAdmin(null);  //set with property above

        return request;
    }

    public String getIsAdminString() {
        return isAdminString;
    }

}
