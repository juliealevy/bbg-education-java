package com.play.java.bbgeducation.api.sessions.links;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.sessions.SessionRequest;

public class ApiSessionRequest extends SessionRequest {
    private static final String stringType = String.class.getSimpleName().toLowerCase();
    private static final String datePattern = "MM-dd-yyyy";

    @JsonProperty("startDate")
    private String startDateString = datePattern;
    @JsonProperty("endDate")
    private String endDateString = datePattern;
    @JsonProperty("practicumHours")
    public String practicumHoursStr = Integer.class.getSimpleName();


    public String getStartDateString(){
        return startDateString;
    }

    public String getEndDateString(){
        return endDateString;
    }

    public String getPracticumHoursStr(){
        return practicumHoursStr;
    }

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
