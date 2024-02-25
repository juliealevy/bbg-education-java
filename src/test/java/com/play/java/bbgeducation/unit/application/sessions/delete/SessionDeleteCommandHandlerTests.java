package com.play.java.bbgeducation.unit.application.sessions.delete;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.delete.SessionDeleteCommand;
import com.play.java.bbgeducation.application.sessions.delete.SessionDeleteCommandHandler;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionDeleteCommandHandlerTests {
    private final SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final SessionCacheManager cacheManager = Mockito.mock(SessionCacheManager.class);
    private SessionDeleteCommandHandler underTest;

    public SessionDeleteCommandHandlerTests(){
        underTest = new SessionDeleteCommandHandler(
                sessionRepository, cacheManager);
    }

    @Test
    public void handle_ShouldReturnSuccess_WhenIdExists() {
        SessionDeleteCommand command = Instancio.create(SessionDeleteCommand.class);
        SessionEntity session = Instancio.create(SessionEntity.class);

        when(sessionRepository.getByProgramIdAndId(command.getProgramId(), command.getSessionId()))
                .thenReturn(Optional.of(session));
        doNothing().when(sessionRepository).delete(session);
        doNothing().when(cacheManager).removeSession(any(Long.class), any(Long.class));

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExist() {
        SessionDeleteCommand command = Instancio.create(SessionDeleteCommand.class);

        when(sessionRepository.getByProgramIdAndId(command.getProgramId(), command.getSessionId()))
                .thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }
}
