package com.play.java.bbgeducation.application.programs.getById;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.programs.result.ProgramResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramGetByIdCommandHandler
        implements Command.Handler<ProgramGetByIdCommand, OneOf2<ProgramResult, NotFound>> {
    private final ProgramRepository programRepository;

    public ProgramGetByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public OneOf2<ProgramResult, NotFound> handle(ProgramGetByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }

        return OneOf2.fromOption1(ProgramResult.builder()
                .id(found.get().getId())
                .name(found.get().getName())
                .description(found.get().getDescription())
                .build());


    }
}
