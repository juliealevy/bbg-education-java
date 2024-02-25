package com.play.java.bbgeducation.application.sessions.create;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionCreateCommandHandler implements Command.Handler<SessionCreateCommand, OneOf3<SessionResult, NotFound, ValidationFailed>>{

    private final SessionRepository sessionRepository;
    private final SessionCacheManager sessionCacheManager;
    private final ProgramRepository programRepository;
    private final Mapper<SessionEntity, SessionResult> mapper;

    public SessionCreateCommandHandler(SessionRepository sessionRepository, SessionCacheManager sessionCacheManager, ProgramRepository programRepository, Mapper<SessionEntity, SessionResult> mapper) {
        this.sessionRepository = sessionRepository;
        this.sessionCacheManager = sessionCacheManager;
        this.programRepository = programRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf3<SessionResult, NotFound, ValidationFailed> handle(SessionCreateCommand command) {

        //does program exist
        Optional<ProgramEntity> program = programRepository.findById(command.getProgramId());
        if (program.isEmpty()){
            return OneOf3.fromOption2(new NotFound());
        }
        //does session name exist
        if (sessionRepository.existsByName(command.getName())){
            return OneOf3.fromOption3(new NameExistsValidationFailed("session"));
        }

        //do start and end date ranges need to not overlap??  might be configuration...

        SessionEntity session = SessionEntity.builder()
                .program(program.get())
                .name(command.getName())
                .description(command.getDescription())
                .startDate(command.getStartDate())
                .endDate(command.getEndDate())
                .practicumHours(command.getPracticumHours())
                        .build();

        SessionEntity saved = sessionRepository.save(session);
        SessionResult sessionResult = mapper.mapTo(saved);
        sessionCacheManager.cacheSession(sessionResult);


        return OneOf3.fromOption1(sessionResult);
    }
}
