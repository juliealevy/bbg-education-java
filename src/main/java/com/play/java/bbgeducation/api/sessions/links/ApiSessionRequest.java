package com.play.java.bbgeducation.api.sessions.links;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.api.common.ApiBodyType;
import com.play.java.bbgeducation.api.sessions.SessionRequest;

public class ApiSessionRequest extends SessionRequest {

    @JsonProperty("startDate")
    private String startDateString = ApiBodyType.DATE.value;
    @JsonProperty("endDate")
    private String endDateString = ApiBodyType.DATE.value;
    @JsonProperty("practicumHours")
    public String practicumHoursStr = ApiBodyType.INTEGER.value;


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
       request.setName(ApiBodyType.STRING.value);
       request.setDescription(ApiBodyType.STRING.value);
       request.setStartDate(null);
       request.setEndDate(null);
       request.setPracticumHours(null);
       return request;
    }


}
