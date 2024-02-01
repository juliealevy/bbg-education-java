package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.oneof.OneOf2;
import com.play.java.bbgeducation.application.exceptions.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.exceptions.ValidationFailed;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class ProgramCreateCommandHandler
//        implements RequestHandler<ProgramCreateCommand, ProgramResult>
            implements Command.Handler<ProgramCreateCommand, OneOf2<ProgramResult, ValidationFailed>>
{

    private final ProgramRepository programRepository;

    public ProgramCreateCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @SneakyThrows
    @Override
    public OneOf2<ProgramResult, ValidationFailed> handle(ProgramCreateCommand createProgramCommand) {

        //data validation
        //check whether program with name already exists
        if (programRepository.existsByName(createProgramCommand.getName())){
                return OneOf2.fromOption2(new NameExistsValidationFailed("program"));
        }
        ProgramEntity programEntity = ProgramEntity.builder()
                .name(createProgramCommand.getName())
                .description(createProgramCommand.getDescription())
                .build();
        ProgramEntity newProgram = programRepository.save(programEntity);

        return OneOf2.fromOption1(ProgramResult.builder()
                .id(newProgram.getId())
                .name(newProgram.getName())
                .description(newProgram.getDescription())
                .build());
    }
}
