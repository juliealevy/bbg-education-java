package com.play.java.bbgeducation.application.programs.commands;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.exceptions.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.OneOfTypes;
import com.play.java.bbgeducation.domain.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramUpdateCommandHandler implements Command.Handler<ProgramUpdateCommand,
        OneOf3<OneOfTypes.Success, OneOfTypes.NotFound, ValidationFailed>>{
    private final ProgramRepository programRepository;

    public ProgramUpdateCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @SneakyThrows
    @Override
    public OneOf3<OneOfTypes.Success, OneOfTypes.NotFound, ValidationFailed> handle(ProgramUpdateCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf3.fromOption2(new OneOfTypes.NotFound());
        }

        if (!found.get().getName().equals(command.getName())){
            Optional<ProgramEntity> match = programRepository.findByName(command.getName());
            if (match.isPresent()){
                return OneOf3.fromOption3(new NameExistsValidationFailed("program"));
            }
        }
        ProgramEntity entityToSave = ProgramEntity.builder()
                        .id(command.getId())
                        .name(command.getName())
                        .description(command.getDescription())
                        .build();

        ProgramEntity saved = programRepository.save(entityToSave);

        return OneOf3.fromOption1(new OneOfTypes.Success());

    }
}
