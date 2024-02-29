package com.play.java.bbgeducation.integration.api.programs;

import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.programs.create.ProgramCreateCommand;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;

public final class DataUtils {

    private DataUtils(){}

    public static ProgramEntity buildProgramI(){
            return  ProgramEntity.create(
                    "Horticultural Certificate Program I",
                    "This is the blah blah program I");
    }

    public static ProgramEntity buildProgramII(){
        return  ProgramEntity.create(
                "Horticultural Certificate Program II",
                "This is the blah blah program II");
    }

    public static ProgramCreateCommand buildCreateCommandI(){
        return  ProgramCreateCommand.builder()
                .name("Horticultural Certificate Program I")
                .description("This is the blah blah program I")
                .build();
    }

    public static ProgramCreateCommand buildCreateCommandII(){
        return  ProgramCreateCommand.builder()
                .name("Horticultural Certificate Program II")
                .description("This is the blah blah program II")
                .build();
    }

    public static ProgramRequest buildRequestI(){
        return  ProgramRequest.builder()
                .name("Horticultural Certificate Program I")
                .description("This is the blah blah program I")
                .build();
    }

    public static ProgramRequest buildRequestII(){
        return  ProgramRequest.builder()
                .name("Horticultural Certificate Program II")
                .description("This is the blah blah program II")
                .build();
    }

}
