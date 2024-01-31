package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.programs.ProgramResult;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramDeleteByIdCommandHandler
        implements Command.Handler<ProgramDeleteByIdCommand, Optional<ProgramResult>> {
    private final ProgramRepository programRepository;

    public ProgramDeleteByIdCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public Optional<ProgramResult> handle(ProgramDeleteByIdCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return Optional.empty();
        }
        programRepository.deleteById(command.getId());

        return Optional.of(ProgramResult.builder()
                .id(found.get().getId())
                .name(found.get().getName())
                .description(found.get().getDescription())
                .build());

    }
}
