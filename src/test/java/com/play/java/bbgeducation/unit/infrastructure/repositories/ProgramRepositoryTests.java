package com.play.java.bbgeducation.unit.infrastructure.repositories;

import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import com.play.java.bbgeducation.integration.api.programs.DataUtils;
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
public class ProgramRepositoryTests {
    private final ProgramRepository underTest;

    @Autowired
    public ProgramRepositoryTests(ProgramRepository underTest) {
        this.underTest = underTest;
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

    @Test
    public void ExistsByName_ShouldReturnTrue_WhenExists(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);

        boolean result = underTest.existsByName(program1.getName());

        assertThat(result).isTrue();
    }

    @Test
    public void ExistsByName_ShouldReturnFalse_WhenNotExists(){
        ProgramEntity program1 = DataUtils.buildProgramI();
        underTest.save(program1);

        boolean result = underTest.existsByName(program1.getName() + "wrong");

        assertThat(result).isFalse();
    }
}
