package com.play.java.bbgeducation.unit.infrastructure.repositories;

import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.infrastructure.repositories.SessionRepository;
import com.play.java.bbgeducation.integration.api.programs.DataUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionRepositoryTests {
    private final ProgramRepository programRepository;
    private final SessionRepository underTest;

    @Autowired
    public SessionRepositoryTests(ProgramRepository programRepository, SessionRepository underTest) {
        this.programRepository = programRepository;
        this.underTest = underTest;
    }

    @Test
    public void ExistsByName_ShouldReturnTrue_WhenExists(){
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);
        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        boolean result = underTest.existsByName(savedSession.getName());

        assertThat(result).isTrue();
    }

    @Test
    public void ExistsByName_ShouldReturnFalse_WhenNotExists(){
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity savedProgram = programRepository.save(program);
        SessionEntity create = buildSessionEntity(savedProgram);
        SessionEntity savedSession = underTest.save(create);

        boolean result = underTest.existsByName(savedSession.getName() + "wrong");

        assertThat(result).isFalse();
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
    public void Delete_ShouldSucceed_WhenSessionNotExists() {
        SessionEntity session = Instancio.create(SessionEntity.class);

        Optional<SessionEntity> sessionToDelete = underTest.getByProgramIdAndId(session.getProgram().getId(), session.getId());

        underTest.delete(session);

        assertThat(sessionToDelete).isEmpty();

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

    private SessionEntity buildSessionEntity2(ProgramEntity program){
        return SessionEntity.create(
                "Test SessionEntity Two",
                "Test description two",
                program,
                LocalDate.now().plusMonths(5),
                LocalDate.now().plusMonths(10),
                20);
    }
}
