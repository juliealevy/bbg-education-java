package com.play.java.bbgeducation.application.sessions.update;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionUpdateCommandHandler implements Command.Handler<SessionUpdateCommand, OneOf3<Success, NotFound, ValidationFailed>> {

    private final SessionRepository sessionRepository;
    private final SessionCacheManager cacheManager;
    private final Mapper<SessionEntity, SessionResult> mapper;

    public SessionUpdateCommandHandler(SessionRepository sessionRepository, SessionCacheManager cacheManager, Mapper<SessionEntity, SessionResult> mapper) {
        this.sessionRepository = sessionRepository;
        this.cacheManager = cacheManager;
        this.mapper = mapper;
    }

    @Override
    public OneOf3<Success, NotFound, ValidationFailed> handle(SessionUpdateCommand command) {

        Optional<SessionEntity> found = sessionRepository.getByProgramIdAndId(command.getProgramId(), command.getId());
        if (found.isEmpty()){
            return OneOf3.fromOption2(new NotFound());
        }

        if (!command.getName().equals(found.get().getName()) && sessionRepository.existsByName(command.getName())){
            return OneOf3.fromOption3(new NameExistsValidationFailed("session"));
        }

        SessionEntity session = SessionEntity.builder()
                .id(command.getId())
                .name(command.getName())
                .description(command.getDescription())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .practicumHours(command.getPracticumHours())
                .program(found.get().getProgram())
                .build();

        sessionRepository.save(session);
        cacheManager.cacheSession(mapper.mapTo(session));
        return OneOf3.fromOption1(new Success());
    }
}
