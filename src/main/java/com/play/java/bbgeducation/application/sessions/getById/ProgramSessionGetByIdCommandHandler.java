package com.play.java.bbgeducation.application.sessions.getById;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProgramSessionGetByIdCommandHandler implements Command.Handler<ProgramSessionGetByIdCommand,
        OneOf2<SessionResult, NotFound>>{

    private final SessionRepository sessionRepository;
    private final Mapper<SessionEntity, SessionResult> mapper;

    public ProgramSessionGetByIdCommandHandler(SessionRepository sessionRepository, Mapper<SessionEntity, SessionResult> mapper) {
        this.sessionRepository = sessionRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf2<SessionResult, NotFound> handle(ProgramSessionGetByIdCommand command) {
        Optional<SessionEntity> session = sessionRepository.getByProgramIdAndId(
                command.getProgramId(), command.getSessionId());

        return session.<OneOf2<SessionResult, NotFound>>map(
                sessionEntity -> OneOf2.fromOption1(mapper.mapTo(sessionEntity)))
                .orElseGet(() -> OneOf2.fromOption2(new NotFound()));


    }
}
