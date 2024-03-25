package com.play.java.bbgeducation.unit.application.sessions.addCourse;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.sessions.addCourse.AddCourseCommand;
import com.play.java.bbgeducation.application.sessions.addCourse.AddCourseCommandHandler;
import com.play.java.bbgeducation.application.sessions.caching.SessionCacheManager;
import com.play.java.bbgeducation.application.sessions.caching.SessionGetCacheManager;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionCourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import com.play.java.bbgeducation.integration.api.programs.DataUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddCourseCommandHandlerTest {
    private AddCourseCommandHandler underTest;
    private SessionCourseRepository sessionCourseRepository = Mockito.mock(SessionCourseRepository.class);
    private SessionRepository sessionRepository = Mockito.mock(SessionRepository.class);
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private SessionGetCacheManager sessionCacheManager = Mockito.mock(SessionGetCacheManager.class);
    CourseEntity course;
    SessionEntity session;
    ProgramEntity program;
    @BeforeEach
    void setUp() {
        underTest = new AddCourseCommandHandler(sessionRepository, courseRepository, sessionCourseRepository, sessionCacheManager);
        program = Instancio.create(ProgramEntity.class);
        session = Instancio.create(SessionEntity.class);
        session.setProgram(program);
        course = Instancio.create(CourseEntity.class);
    }

    @Test
    public void handle_shouldAddCourse_whenAllisValid(){
        AddCourseCommand command = new AddCourseCommand(program.getId(), session.getId(), course.getId());
        SessionCourseEntity sessionCourseEntity = SessionCourseEntity.create(session, course);

        when(sessionRepository.getByProgramIdAndId(program.getId(), session.getId())).thenReturn(Optional.of(session));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(sessionCourseRepository.getBySessionIdAndCourseId(any(Long.class), any(Long.class))).thenReturn(Optional.empty());
        when(sessionCourseRepository.save(any(SessionCourseEntity.class))).thenReturn(sessionCourseEntity);
        doNothing().when(sessionCacheManager).clearSession(command.getProgramId(), command.getSessionId());

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    public void handle_shouldReturnNotFound_whenSessionNotExists(){
        AddCourseCommand command = new AddCourseCommand(program.getId(), session.getId(), course.getId());

        when(sessionRepository.getByProgramIdAndId(program.getId(), session.getId())).thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    public void handle_shouldReturnNotFound_whenCourseNotExists(){
        AddCourseCommand command = new AddCourseCommand(program.getId(), session.getId(), course.getId());

        when(sessionRepository.getByProgramIdAndId(program.getId(), session.getId())).thenReturn(Optional.of(session));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(command);
        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    public void handle_shouldReturnSuccess_whenCourseAlreadyAdded(){
        AddCourseCommand command = new AddCourseCommand(program.getId(), session.getId(), course.getId());
        SessionCourseEntity sessionCourseEntity = SessionCourseEntity.create(session, course);

        when(sessionRepository.getByProgramIdAndId(program.getId(), session.getId())).thenReturn(Optional.of(session));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(sessionCourseRepository.getBySessionIdAndCourseId(any(Long.class), any(Long.class))).thenReturn(Optional.of(sessionCourseEntity));

        OneOf2<Success, NotFound> result = underTest.handle(command);
        assertThat(result.hasOption1()).isTrue();


    }
}