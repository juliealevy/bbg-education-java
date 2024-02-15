package com.play.java.bbgeducation.unit.sessions.commands.getByProgram;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.sessions.getById.ProgramSessionGetByIdCommand;
import com.play.java.bbgeducation.application.sessions.getById.ProgramSessionGetByIdCommandHandler;
import com.play.java.bbgeducation.application.sessions.getByProgram.ProgramSessionGetByProgramCommand;
import com.play.java.bbgeducation.application.sessions.getByProgram.ProgramSessionGetByProgramCommandHandler;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.application.sessions.result.SessionResultMapper;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramSessionGetByProgramCommandHandlerTests {
    ProgramSessionGetByProgramCommandHandler underTest;
    SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);
    private final SessionResultMapper mapper;

    @Autowired
    public ProgramSessionGetByProgramCommandHandlerTests(SessionResultMapper mapper) {
        this.mapper = mapper;
        this.underTest = new ProgramSessionGetByProgramCommandHandler(sessionRepository, programRepository, mapper);
    }

    @Test
    public void handle_ShouldReturnSessions_WhenProgramExists(){
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        List<SessionEntity> sessionList = new ArrayList<>();
        sessionList.add(Instancio.create(SessionEntity.class));
        ProgramSessionGetByProgramCommand command = new ProgramSessionGetByProgramCommand(program.getId());

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(program));
        when(sessionRepository.getByProgramId(program.getId())).thenReturn(sessionList);

        OneOf2<List<SessionResult>, NotFound> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().size()).isEqualTo(1);

    }

    @Test
    public void handle_ShouldReturnNotFound_WhenProgramNotExist(){
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        ProgramSessionGetByProgramCommand command = new ProgramSessionGetByProgramCommand(program.getId());

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf2<List<SessionResult>, NotFound> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();

    }


}
