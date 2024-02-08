package com.play.java.bbgeducation.application.programs.delete;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramDeleteByIdCommandHandler
        implements Command.Handler<ProgramDeleteByIdCommand, OneOf2<Success, NotFound>> {
    private final ProgramRepository programRepository;

    public ProgramDeleteByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public OneOf2<Success, NotFound> handle(ProgramDeleteByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }
        programRepository.deleteById(command.getId());
        return OneOf2.fromOption1(new Success());
    }
}
