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
public class ProgramDeleteByIdCommandHandler
        implements Command.Handler<ProgramDeleteByIdCommand, OneOf2<OneOfTypes.Success, OneOfTypes.NotFound>> {
    private final ProgramRepository programRepository;

    public ProgramDeleteByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public OneOf2<OneOfTypes.Success, OneOfTypes.NotFound> handle(ProgramDeleteByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf2.fromOption2(new OneOfTypes.NotFound());
        }
        programRepository.deleteById(command.getId());
        return OneOf2.fromOption1(new OneOfTypes.Success());
    }
}
