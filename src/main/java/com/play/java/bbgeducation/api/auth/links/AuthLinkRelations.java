package com.play.java.bbgeducation.api.auth.links;

public enum AuthLinkRelations {

    REGISTER("auth:register"),
    LOGIN("auth:login"),
    REFRESH("auth:refresh");

    public final String value;
    private AuthLinkRelations(String value){
        this.value = value;
    }
}
