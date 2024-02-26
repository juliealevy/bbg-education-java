package com.play.java.bbgeducation.application.sessions.getById;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.caching.SessionGetCacheManager;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramSessionGetByIdCommandHandler implements Command.Handler<ProgramSessionGetByIdCommand,
        OneOf2<SessionResult, NotFound>>{

    private final SessionGetCacheManager cacheManager;
    private final SessionRepository sessionRepository;
    private final Mapper<SessionEntity, SessionResult> mapper;


    public ProgramSessionGetByIdCommandHandler(SessionGetCacheManager cacheManager, SessionRepository sessionRepository, Mapper<SessionEntity, SessionResult> mapper) {
        this.cacheManager = cacheManager;
        this.sessionRepository = sessionRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf2<SessionResult, NotFound> handle(ProgramSessionGetByIdCommand command) {
        Optional<SessionResult> sessionResult = cacheManager.getSession(
                command.getProgramId(), command.getSessionId());

        if (sessionResult.isPresent()) {
            return OneOf2.fromOption1(sessionResult.get());
        }

        Optional<SessionEntity> session = sessionRepository.getByProgramIdAndId(
                command.getProgramId(), command.getSessionId());

        if (session.isEmpty()) {
            return OneOf2.fromOption2(new NotFound());
        }

        SessionResult mappedSession = mapper.mapTo(session.get());
        cacheManager.cacheSession(mappedSession);
        return OneOf2.fromOption1(mappedSession);
    }
}
