package com.play.java.bbgeducation.api.sessioncourses.links;

public enum SessionCourseLinkRelations {
    ADD_COURSE("session:add-course")
    ;

    public final String value;
    private SessionCourseLinkRelations(String value){
        this.value = value;
    }
}
