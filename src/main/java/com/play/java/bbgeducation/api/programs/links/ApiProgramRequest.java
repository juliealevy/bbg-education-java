package com.play.java.bbgeducation.api.programs.links;

import com.play.java.bbgeducation.api.common.ApiBodyType;
import com.play.java.bbgeducation.api.programs.ProgramRequest;

public class ApiProgramRequest extends ProgramRequest {
    public static ProgramRequest getApiBody(){

        return ProgramRequest.builder()
                .name(ApiBodyType.STRING.value)
                .description(ApiBodyType.STRING.value)
                .build();


    }
}
