package com.play.java.bbgeducation.integration.programs;

import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.application.programs.commands.ProgramCreateCommand;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;

public final class DataUtils {

    private DataUtils(){}

    public static ProgramEntity buildProgramI(){
            return  ProgramEntity.builder()
                    .name("Horticultural Certificate Program I")
                    .description("This is the blah blah program I")
                    .build();
    }

    public static ProgramEntity buildProgramII(){
        return  ProgramEntity.builder()
                .name("Horticultural Certificate Program II")
                .description("This is the blah blah program II")
                .build();
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
