package com.play.java.bbgeducation.api.courses.links;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.common.ApiBodyType;
import com.play.java.bbgeducation.api.courses.CourseRequest;

public class ApiCourseRequest extends CourseRequest {

    @JsonProperty("isPublic")
    private String isPublicString = ApiBodyType.BOOLEAN.value;

    @JsonProperty("isOnline")
    private String isOnlineString = ApiBodyType.BOOLEAN.value;;

    public String getIsPublicString(){
        return isPublicString;
    }

    public String getIsOnlineString(){
        return isOnlineString;
    }


    public static CourseRequest getApiBody(){
        ApiCourseRequest request = new ApiCourseRequest();
        request.setName(ApiBodyType.STRING.value);
        request.setDescription(ApiBodyType.STRING.value);
        request.setIsOnline(null);
        request.setIsPublic(null);
        return request;
    }
}
