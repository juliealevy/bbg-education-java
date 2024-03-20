package com.play.java.bbgeducation.api.auth.links;

public enum AuthLinkRelations {

    REGISTER("auth:register"),
    LOGIN("auth:login"),
    REFRESH("auth:refresh"),
    UPDATE_USERNAME("auth:update-username"),
    UPDATE_PASSWORD("auth:update-password");


    public final String value;
    private AuthLinkRelations(String value){
        this.value = value;
    }
}
