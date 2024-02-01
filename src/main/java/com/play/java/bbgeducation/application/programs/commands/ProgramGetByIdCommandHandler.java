package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOfTypes;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramGetByIdCommandHandler
        implements Command.Handler<ProgramGetByIdCommand, OneOf2<ProgramResult, OneOfTypes.NotFound>> {
    private final ProgramRepository programRepository;

    public ProgramGetByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public OneOf2<ProgramResult, OneOfTypes.NotFound> handle(ProgramGetByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf2.fromOption2(new OneOfTypes.NotFound());
        }

        return OneOf2.fromOption1(ProgramResult.builder()
                .id(found.get().getId())
                .name(found.get().getName())
                .description(found.get().getDescription())
                .build());


    }
}
