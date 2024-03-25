package com.play.java.bbgeducation.application.sessions.getByProgram;

import an.awesome.pipelinr.Command;
import com.play.java.bbgeducation.application.common.mapping.MapTo;
import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProgramSessionGetByProgramCommandHandler implements Command.Handler<ProgramSessionGetByProgramCommand,
        OneOf2<List<SessionResult>, NotFound>>{

    private final SessionRepository sessionRepository;
    private final ProgramRepository programRepository;
    private final MapTo<SessionEntity, SessionResult> mapper;

    public ProgramSessionGetByProgramCommandHandler(SessionRepository sessionRepository, ProgramRepository programRepository, MapTo<SessionEntity, SessionResult> mapper) {
        this.sessionRepository = sessionRepository;
        this.programRepository = programRepository;
        this.mapper = mapper;
    }

    @Override
    public OneOf2<List<SessionResult>, NotFound> handle(ProgramSessionGetByProgramCommand command) {

        if (programRepository.findById(command.getProgramId()).isEmpty()){
            return OneOf2.fromOption2(new NotFound());
        }
        List<SessionEntity> sessions = sessionRepository.getByProgramId(command.getProgramId());

        return OneOf2.fromOption1(sessions
                .stream().map(mapper::mapTo)
                .toList());
    }
}
