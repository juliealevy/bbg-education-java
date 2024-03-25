package com.play.java.bbgeducation.api.sessions.links;

public enum SessionLinkRelations {
    CREATE("session:create"),
    UPDATE ("session:update"),
    DELETE("session:delete"),
    GET_BY_ID("session:get-by-id"),
    GET_BY_PROGRAM ("session:get-by-program")

    ;

    public final String value;
    private SessionLinkRelations(String value){
        this.value = value;
    }
}
