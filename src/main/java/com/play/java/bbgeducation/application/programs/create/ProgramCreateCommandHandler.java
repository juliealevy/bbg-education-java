package com.play.java.bbgeducation.application.programs.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

@Component
public class ProgramCreateCommandHandler
        implements Command.Handler<ProgramCreateCommand, OneOf2<ProgramResult, ValidationFailed>>
{
    private final ProgramRepository programRepository;

    public ProgramCreateCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public OneOf2<ProgramResult, ValidationFailed> handle(ProgramCreateCommand createProgramCommand) {

        if (programRepository.existsByName(createProgramCommand.getName())) {
            return OneOf2.fromOption2(new NameExistsValidationFailed("program"));
        }

        ProgramEntity programEntity = ProgramEntity.create(
                        createProgramCommand.getName(),
                        createProgramCommand.getDescription());

        ProgramEntity newProgram = programRepository.save(programEntity);

        return OneOf2.fromOption1(ProgramResult.builder()
                .id(newProgram.getId())
                .name(newProgram.getName())
                .description(newProgram.getDescription())
                .build());
    }

}
