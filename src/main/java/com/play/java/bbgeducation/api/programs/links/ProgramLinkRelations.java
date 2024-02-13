package com.play.java.bbgeducation.api.programs.links;

public enum ProgramLinkRelations {
    CREATE("program:create"),
    UPDATE ("program:update"),
    DELETE("program:delete"),
    GET_BY_ID("program:get-by-id"),
    GET_ALL ("program:get-all");
;

    public final String value;
    private ProgramLinkRelations(String value){
        this.value = value;
    }
}
