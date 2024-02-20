package com.play.java.bbgeducation.unit.application.sessions.create;

import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommand;
import com.play.java.bbgeducation.application.sessions.create.SessionCreateCommandHandler;
import com.play.java.bbgeducation.application.sessions.result.SessionResultMapper;
import com.play.java.bbgeducation.application.sessions.result.SessionResult;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
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
public class ProgramSessionCreateCommandHandlerTests {
    private SessionCreateCommandHandler underTest;
    private ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);
    private SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final SessionResultMapper mapper;

    @Autowired
    public ProgramSessionCreateCommandHandlerTests(SessionResultMapper mapper) {
        this.mapper = mapper;
    }

    @BeforeEach
    public void init(){
        underTest = new SessionCreateCommandHandler(sessionRepository, programRepository, mapper);
    }

    @Test
    public void handle_ShouldReturnSessionResult_WhenInputValid(){
        SessionCreateCommand cmd = Instancio.create(SessionCreateCommand.class);
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        program.setId(cmd.getProgramId());

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(program));
        when(sessionRepository.existsByName(any(String.class))).thenReturn(false);
        when(sessionRepository.save(any(SessionEntity.class))).then(returnsFirstArg());

        OneOf3<SessionResult, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getName()).isEqualTo(cmd.getName());
        assertThat(result.asOption1().getDescription()).isEqualTo(cmd.getDescription());
        assertThat(result.asOption1().getStartDate()).isEqualTo(cmd.getStartDate());
        assertThat(result.asOption1().getEndDate()).isEqualTo(cmd.getEndDate());
        assertThat(result.asOption1().getPracticumHours()).isEqualTo(cmd.getPracticumHours());
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenProgramNotExist(){
        SessionCreateCommand cmd = Instancio.create(SessionCreateCommand.class);

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf3<SessionResult, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void handle_ShouldReturnValidationFailed_WhenNameExists(){
        SessionCreateCommand cmd = Instancio.create(SessionCreateCommand.class);
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        program.setId(cmd.getProgramId());

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(program));
        when(sessionRepository.existsByName(any(String.class))).thenReturn(true);

        OneOf3<SessionResult, NotFound, ValidationFailed> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
    }

}
