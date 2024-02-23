package com.play.java.bbgeducation.api.users.links;

public enum UserLinkRelations {

    UPDATE ("user:update"),
    DELETE("user:delete"),
    GET_BY_ID("user:get-by-id"),
    GET_ALL("user:get-all");

    public final String value;
    private UserLinkRelations(String value){
        this.value = value;
    }
}
