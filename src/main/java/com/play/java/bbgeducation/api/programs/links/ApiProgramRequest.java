package com.play.java.bbgeducation.api.programs.links;

import com.play.java.bbgeducation.api.programs.ProgramRequest;

public class ApiProgramRequest extends ProgramRequest {
    private static final String stringType = String.class.getSimpleName().toLowerCase();
    public static ProgramRequest getApiBody(){

        return ProgramRequest.builder()
                .name(stringType)
                .description(stringType)
                .build();


    }
}
