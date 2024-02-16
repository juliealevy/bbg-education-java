package com.play.java.bbgeducation.api.courses.links;

public enum CourseLinkRelations {
    CREATE("course:create"),
    UPDATE ("course:update"),
    DELETE("course:delete"),
    GET_BY_ID("course:get-by-id"),
    GET_ALL ("course:get-all");
    ;

    public final String value;
    private CourseLinkRelations(String value){
        this.value = value;
    }
}
