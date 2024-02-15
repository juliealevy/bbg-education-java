package com.play.java.bbgeducation.api.sessions.links;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.sessions.SessionRequest;
import lombok.Getter;


public class ApiSessionRequest extends SessionRequest {
    private static final String stringType = String.class.getSimpleName().toLowerCase();
    private static final String datePattern = "MM-dd-yyyy";

    @JsonProperty("startDate")
    @Getter
    private String startDateStr = datePattern;
    @JsonProperty("endDate")
    @Getter
    private String endDateStr = datePattern;

    @JsonProperty("practicumHours")
    @Getter
    private String practicumHoursStr = Integer.class.getSimpleName();

    public static SessionRequest getApiBody(){
        ApiSessionRequest request = new ApiSessionRequest();
       request.setName(stringType);
       request.setDescription(stringType);
       request.setStartDate(null);
       request.setEndDate(null);
       request.setPracticumHours(null);
       return request;
    }

}
