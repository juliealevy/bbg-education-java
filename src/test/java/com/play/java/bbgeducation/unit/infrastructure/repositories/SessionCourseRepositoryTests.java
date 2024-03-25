package com.play.java.bbgeducation.unit.infrastructure.repositories;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionCourseRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import com.play.java.bbgeducation.integration.api.programs.DataUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionCourseRepositoryTests {

    private final SessionCourseRepository underTest;
    private final ProgramRepository programRepository;
    private final SessionRepository sessionRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public SessionCourseRepositoryTests(SessionCourseRepository underTest, ProgramRepository programRepository, SessionRepository sessionRepository, CourseRepository courseRepository) {
        this.underTest = underTest;
        this.programRepository = programRepository;
        this.sessionRepository = sessionRepository;
        this.courseRepository = courseRepository;
    }

    @BeforeEach
    void setUp() {

    }

    @Test
    @Transactional
    public void GetById_ShouldReturnResult_WhenSessionCourseExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity session = buildSessionEntity(savedProgram);
        SessionEntity savedSession = sessionRepository.save(session);

        CourseEntity course = CourseEntity.create("TestCourse", "blah", true, true);
        CourseEntity savedCourse = courseRepository.save(course);

        SessionCourseEntity sessionCourse = SessionCourseEntity.create(savedSession, savedCourse);
        SessionCourseEntity savedSessionCourse = underTest.save(sessionCourse);

        Optional<SessionCourseEntity> result = underTest.getBySessionIdAndCourseId(session.getId(), course.getId());

        assertThat(result).isNotNull();
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedSessionCourse);
    }

    @Test
    @Transactional
    public void GetById_ShouldReturnNotFound_WhenSessionCourseNotExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity session = buildSessionEntity(savedProgram);
        SessionEntity savedSession = sessionRepository.save(session);

        CourseEntity course = CourseEntity.create("TestCourse", "blah", true, true);
        CourseEntity savedCourse = courseRepository.save(course);

        SessionCourseEntity sessionCourse = SessionCourseEntity.create(savedSession, savedCourse);
        //not saved

        Optional<SessionCourseEntity> result = underTest.getBySessionIdAndCourseId(session.getId(), course.getId());

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    private SessionEntity buildSessionEntity(ProgramEntity program){
        return SessionEntity.create(
                "Test SessionEntity",
                "Test description",
                program,
                LocalDate.now().plusMonths(1),
                LocalDate.now().plusMonths(7),
                15);

    }

}
