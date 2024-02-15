package com.play.java.bbgeducation.integration.sessions;

import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import org.hibernate.TransientPropertyValueException;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionRepositoryIntegrationTests {
    private final ProgramRepository programRepository;
    private final SessionRepository underTest;

    @Autowired
    public SessionRepositoryIntegrationTests(ProgramRepository programRepository, SessionRepository underTest) {
        this.programRepository = programRepository;
        this.underTest = underTest;
    }

    @Test
    public void CreateSession_ShouldSucceedAndFetch_WhenInputValid() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);

        SessionEntity savedSession = underTest.save(create);

        Optional<SessionEntity> fetched = underTest.getByProgramIdAndId(savedProgram.getId(), savedSession.getId());

        assertThat(fetched).isPresent();
        assertThat(savedSession).isEqualTo(create);
    }

    @Test
    public void CreateSessionMany_ShouldSucceedAndFetch_WhenInputValid() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create1 = buildSessionEntity(savedProgram);
        SessionEntity savedSession1 = underTest.save(create1);

        SessionEntity create2 = buildSessionEntity2(savedProgram);
        SessionEntity savedSession2 = underTest.save(create2);

        List<SessionEntity> sessions = underTest.getByProgramId(savedProgram.getId());

        assertThat(sessions).isNotNull();
        assertThat(sessions).hasSize(2);
    }

    @Test
    public void CreateSession_ShouldFail_WhenProgramNotExist() {
        ProgramEntity program = DataUtils.buildProgramI();
        SessionEntity create = buildSessionEntity(program);

        assertThatThrownBy(() -> underTest.save(create))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

    }

    @Test
    public void CreateSession_ShouldFail_WhenNameExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create1 = buildSessionEntity(savedProgram);
        SessionEntity savedSession1 = underTest.save(create1);

        SessionEntity create2 = buildSessionEntity2(savedProgram);
        create2.setName(savedSession1.getName());

        assertThatThrownBy(() -> underTest.save(create2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    @Transactional
    public void GetById_ShouldReturnResult_WhenProgramAndSessionExist() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        Optional<SessionEntity> result = underTest.getByProgramIdAndId(savedProgram.getId(), savedSession.getId());


        assertThat(result).isNotNull();
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(savedSession);
    }

    @Test
    @Transactional
    public void GetById_ShouldFail_WhenProgramNotExist() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        Optional<SessionEntity> fetch = underTest.getByProgramIdAndId(savedProgram.getId() + 10L, savedSession.getId());

        assertThat(fetch).isEmpty();
    }

    @Test
    @Transactional
    public void GetById_ShouldFail_WhenSessionNotExist() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        Optional<SessionEntity> fetch = underTest.getByProgramIdAndId(savedProgram.getId(), savedSession.getId()+ 10L);

        assertThat(fetch).isEmpty();
    }

    @Test
    @Transactional
    public void GetById_ShouldFail_WhenNeitherExist() {

        Optional<SessionEntity> fetch = underTest.getByProgramIdAndId(100L, 100L);

        assertThat(fetch).isEmpty();
    }

    @Test
    @Transactional
    public void GetByProgram_ShouldReturnResult_WhenProgramExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        List<SessionEntity> sessions = underTest.getByProgramId(savedProgram.getId());

        assertThat(sessions).isNotNull();
        assertThat(sessions).hasSize(1);
    }

    @Test
    @Transactional
    public void GetByProgram_CanReturnEmpty_WhenProgramExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        List<SessionEntity> sessions = underTest.getByProgramId(savedProgram.getId());

        assertThat(sessions).isNotNull();
        assertThat(sessions).hasSize(0);
    }

    @Test
    @Transactional
    public void GetByProgram_ShouldReturnEmpty_WhenProgramNotExists() {

        List<SessionEntity> sessions = underTest.getByProgramId(100L);

        assertThat(sessions).isNotNull();
        assertThat(sessions).hasSize(0);
    }

    @Test
    @Transactional
    public void Delete_ShouldSucceed_WhenSessionExists() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);

        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        Optional<SessionEntity> fetchedBefore = underTest.getByProgramIdAndId(savedSession.getProgram().getId(), savedSession.getId());
        underTest.delete(fetchedBefore.get());
        Optional<SessionEntity> fetchedAfter = underTest.getByProgramIdAndId(savedSession.getProgram().getId(), savedSession.getId());

        assertThat(fetchedBefore).isPresent();
        assertThat(fetchedAfter).isEmpty();

    }

    @Test
    @Transactional
    public void Delete_ShouldSucceed_WhenSessionNotExists() {
        SessionEntity session = Instancio.create(SessionEntity.class);

        Optional<SessionEntity> sessionToDelete = underTest.getByProgramIdAndId(session.getProgram().getId(), session.getId());

        underTest.delete(session);

        assertThat(sessionToDelete).isEmpty();

    }


    private SessionEntity buildSessionEntity(ProgramEntity program){
        return SessionEntity.builder()
                .program(program)
                .name("Test SessionEntity")
                .description("Test description")
                .practicumHours(15)
                .startDate(LocalDate.now().plusMonths(1))
                .endDate(LocalDate.now().plusMonths(7))
                .build();
    }

    private SessionEntity buildSessionEntity2(ProgramEntity program){
        return SessionEntity.builder()
                .program(program)
                .name("Test SessionEntity Two")
                .description("Test description two")
                .practicumHours(20)
                .startDate(LocalDate.now().plusMonths(5))
                .endDate(LocalDate.now().plusMonths(10))
                .build();
    }
}
