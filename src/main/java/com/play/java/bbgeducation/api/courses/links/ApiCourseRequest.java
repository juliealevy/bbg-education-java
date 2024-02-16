package com.play.java.bbgeducation.api.courses.links;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.courses.CourseRequest;

public class ApiCourseRequest extends CourseRequest {
    private static final String stringType = String.class.getSimpleName().toLowerCase();

    @JsonProperty("isPublic")
    private String isPublicString = Boolean.class.getSimpleName().toLowerCase();

    @JsonProperty("isOnline")
    private String isOnlineString = Boolean.class.getSimpleName().toLowerCase();

    public String getIsPublicString(){
        return isPublicString;
    }

    public String getIsOnlineString(){
        return isOnlineString;
    }


    public static CourseRequest getApiBody(){
        ApiCourseRequest request = new ApiCourseRequest();
        request.setName(stringType);
        request.setDescription(stringType);
        request.setIsOnline(null);
        request.setIsPublic(null);
        return request;
    }
}
