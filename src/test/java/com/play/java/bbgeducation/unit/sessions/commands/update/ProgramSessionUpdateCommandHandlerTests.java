package com.play.java.bbgeducation.unit.sessions.commands.update;

import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.sessions.result.SessionResultMapper;
import com.play.java.bbgeducation.application.sessions.update.SessionUpdateCommand;
import com.play.java.bbgeducation.application.sessions.update.SessionUpdateCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.instancio.Instancio;
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
public class ProgramSessionUpdateCommandHandlerTests {
    private SessionUpdateCommandHandler underTest;
    private SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);

    @Autowired
    public ProgramSessionUpdateCommandHandlerTests(SessionResultMapper mapper) {
        underTest = new SessionUpdateCommandHandler(sessionRepository);
    }

    @Test
    public void handle_ShouldReturnSuccess_WhenInputValid(){
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        SessionEntity session = Instancio.create(SessionEntity.class);
        session.setProgram(program);

        SessionUpdateCommand command = SessionUpdateCommand.builder()
                .programId(program.getId())
                .id(session.getId())
                .name(session.getName() + " updated")
                .description(session.getDescription())
                .startDate(session.getStartDate())
                .endDate(session.getEndDate())
                .practicumHours(session.getPracticumHours())
                .build();


        when( sessionRepository.getByProgramIdAndId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(session));
        when(sessionRepository.existsByName(any(String.class))).thenReturn(false);
        when(sessionRepository.save(any(SessionEntity.class))).then(returnsFirstArg());

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdsNotExist(){
        when( sessionRepository.getByProgramIdAndId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(Instancio.create(SessionUpdateCommand.class));

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    public void handle_ShouldReturnFail_WhenChangedNameExists(){
        ProgramEntity program = Instancio.create(ProgramEntity.class);
        SessionEntity session = Instancio.create(SessionEntity.class);
        session.setProgram(program);

        SessionUpdateCommand command = SessionUpdateCommand.builder()
                .programId(program.getId())
                .id(session.getId())
                .name(session.getName() + " updated")
                .description(session.getDescription())
                .startDate(session.getStartDate())
                .endDate(session.getEndDate())
                .practicumHours(session.getPracticumHours())
                .build();


        when( sessionRepository.getByProgramIdAndId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.of(session));
        when(sessionRepository.existsByName(any(String.class))).thenReturn(true);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
    }

}
