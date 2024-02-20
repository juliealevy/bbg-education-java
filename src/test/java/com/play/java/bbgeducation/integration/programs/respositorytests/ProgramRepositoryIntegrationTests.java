package com.play.java.bbgeducation.integration.programs.respositorytests;

import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.integration.programs.DataUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProgramRepositoryIntegrationTests {
    private final ProgramRepository underTest;

    @Autowired
    public ProgramRepositoryIntegrationTests(ProgramRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    public void CreateProgram_ShouldSucceedAndFetch_WhenInputValid() {
        ProgramEntity program = DataUtils.buildProgramI();
        ProgramEntity saved = underTest.save(program);

        Optional<ProgramEntity> fetched = underTest.findById(saved.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get()).isEqualTo(program);
    }

    @Test
    @Transactional
    public void CreateProgramMany_ShouldSucceedAndFetch_WhenInputValid(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);
        ProgramEntity program2 = DataUtils.buildProgramII();
        underTest.save(program2);

        Iterable<ProgramEntity> results = underTest.findAll();

        assertThat(results)
                .hasSize(2)
                .containsExactly(program1, program2);
    }

    @Test
    public void UpdateProgram_ShouldSucceedAndFetch_WhenInputValid(){
        ProgramEntity program = DataUtils.buildProgramI();
        underTest.save(program);

        program.setName(program.getName() + " changed");
        underTest.save(program);

        Optional<ProgramEntity> fetched = underTest.findById(program.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get().getId()).isEqualTo(program.getId());
        assertThat(fetched.get().getName()).isEqualTo(program.getName());
        assertThat(fetched.get().getDescription()).isEqualTo(program.getDescription());
    }

    @Test
    @Transactional
    public void DeleteProgram_ShouldSucceed_WhenIdExists(){
        ProgramEntity program = DataUtils.buildProgramI();
        underTest.save(program);
        Optional<ProgramEntity> createdFind = underTest.findById(program.getId());

        underTest.deleteById(program.getId());
        Optional<ProgramEntity> deletedFind = underTest.findById(program.getId());

        assertThat(createdFind).isPresent();
        assertThat(createdFind.get()).isEqualTo(program);
        assertThat(deletedFind).isEmpty();
    }

    @Test
    public void DeleteProgram_ShouldSucceed_WhenIdNotExists(){
        Long programId = 100L;
        Optional<ProgramEntity> programToDelete = underTest.findById(programId);
        underTest.deleteById(programId);

        assertThat(programToDelete).isEmpty();
    }

    @Test
    @Transactional
    public void FindProgramByName_ShouldSucceed_WhenNameFound(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);
        ProgramEntity program2 = DataUtils.buildProgramII();
        underTest.save(program2);

        Optional<ProgramEntity> found = underTest.findByName(program1.getName());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(program1);
    }

    @Test
    public void FindProgramByName_ShouldSucceed_WhenNameExists(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);
        ProgramEntity program2 = DataUtils.buildProgramII();
        underTest.save(program2);

        boolean exists = underTest.existsByName(program1.getName());

        assertThat(exists).isTrue();
    }

    @Test
    public void FindProgramByName_ShouldFail_WhenNotExists(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);

        Optional<ProgramEntity> found = underTest.findByName(program1.getName() + " wrong");

        assertThat(found).isEmpty();
    }
}
