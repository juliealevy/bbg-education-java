package com.play.java.bbgeducation.application.sessions.delete;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionDeleteCommandHandler
        implements Command.Handler<SessionDeleteCommand, OneOf2<Success, NotFound>> {

    private final SessionRepository sessionRepository;

    public SessionDeleteCommandHandler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public OneOf2<Success, NotFound> handle(SessionDeleteCommand command) {
        Optional<SessionEntity> found = sessionRepository.getByProgramIdAndId(command.getProgramId(), command.getSessionId());

        if (found.isEmpty()) {
            return OneOf2.fromOption2(new NotFound());
        }
        sessionRepository.delete(found.get());
        return OneOf2.fromOption1(new Success());

    }
}
