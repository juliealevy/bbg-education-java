package com.play.java.bbgeducation.unit.sessions.commands.getById;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommandHandler;
import com.play.java.bbgeducation.application.sessions.create.SessionResultMapper;
import com.play.java.bbgeducation.application.sessions.getById.ProgramSessionGetByIdCommand;
import com.play.java.bbgeducation.application.sessions.getById.ProgramSessionGetByIdCommandHandler;
import com.play.java.bbgeducation.application.sessions.getById.SessionGetByIdCommand;
import com.play.java.bbgeducation.application.sessions.getById.SessionGetByIdCommandHandler;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import io.jsonwebtoken.security.Jwks;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramSessionGetByIdCommandHandlerTests {
    private ProgramSessionGetByIdCommandHandler underTest;
    private SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final SessionResultMapper mapper;

    @Autowired
    public ProgramSessionGetByIdCommandHandlerTests(SessionResultMapper mapper) {
        this.mapper = mapper;
    }

    @BeforeEach
    public void init(){
        underTest = new ProgramSessionGetByIdCommandHandler(sessionRepository, mapper);
    }

    @Test
    public void handle_ShouldReturnSessionResult_WhenIdsExist(){
        ProgramSessionGetByIdCommand cmd = Instancio.create(ProgramSessionGetByIdCommand.class);
        SessionEntity session = Instancio.create(SessionEntity.class);

        when(sessionRepository.getByProgramIdAndId(cmd.getProgramId(), cmd.getSessionId()))
                .thenReturn(Optional.of(session));

        OneOf2<SessionResult, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getId()).isEqualTo(session.getId());
        assertThat(result.asOption1().getName()).isEqualTo(session.getName());
        assertThat(result.asOption1().getDescription()).isEqualTo(session.getDescription());
        assertThat(result.asOption1().getStartDate()).isEqualTo(session.getStartDate());
        assertThat(result.asOption1().getEndDate()).isEqualTo(session.getEndDate());
        assertThat(result.asOption1().getPracticumHours()).isEqualTo(session.getPracticumHours());
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdsNotExist(){
        ProgramSessionGetByIdCommand cmd = Instancio.create(ProgramSessionGetByIdCommand.class);
        SessionEntity session = Instancio.create(SessionEntity.class);

        when(sessionRepository.getByProgramIdAndId(cmd.getProgramId(), cmd.getSessionId()))
                .thenReturn(Optional.empty());

        OneOf2<SessionResult, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();

    }



}
