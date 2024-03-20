package com.play.java.bbgeducation.api.auth.links.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.auth.RegisterRequest;

public class ApiRegisterRequest  extends RegisterRequest {

    private static final String stringType = String.class.getSimpleName().toLowerCase();
    @JsonProperty("isAdmin")
    private String isAdminString = Boolean.class.getSimpleName().toLowerCase();
    public static RegisterRequest getApiBody(){

        ApiRegisterRequest request = new ApiRegisterRequest();
        request.setEmail(stringType);
        request.setPassword(stringType);
        request.setFirstName(stringType);
        request.setLastName(stringType);
        request.setIsAdmin(null);

        return request;
    }

    public String getIsAdminString() {
        return isAdminString;
    }

}
