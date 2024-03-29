package com.play.java.bbgeducation.application.programs.update;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramUpdateCommandHandler implements Command.Handler<ProgramUpdateCommand,
        OneOf3<Success, NotFound, ValidationFailed>>{
    private final ProgramRepository programRepository;

    public ProgramUpdateCommandHandler(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }


    @Override
    public OneOf3<Success, NotFound, ValidationFailed> handle(ProgramUpdateCommand command) {

        Optional<ProgramEntity> found = programRepository.findById(command.getId());
        if (found.isEmpty()){
            return OneOf3.fromOption2(new NotFound());
        }

        if (!found.get().getName().equals(command.getName())){
            Optional<ProgramEntity> match = programRepository.findByName(command.getName());
            if (match.isPresent()){
                return OneOf3.fromOption3(new NameExistsValidationFailed("program"));
            }
        }
        ProgramEntity entityToSave = ProgramEntity.build(
                command.getId(),
                command.getName(),
                command.getDescription());

        ProgramEntity saved = programRepository.save(entityToSave);

        return OneOf3.fromOption1(new Success());

    }
}
