package com.play.java.bbgeducation.unit.application.programs.delete;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.programs.delete.ProgramDeleteByIdCommand;
import com.play.java.bbgeducation.application.programs.delete.ProgramDeleteByIdCommandHandler;
import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import com.play.java.bbgeducation.infrastructure.repositories.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class ProgramDeleteByIdCommandHandlerTests {
    private final ProgramRepository programRepository = Mockito.mock(ProgramRepository.class);
    private ProgramDeleteByIdCommandHandler underTest;

    @BeforeEach
    public void init(){
        underTest = new ProgramDeleteByIdCommandHandler(programRepository);
    }

    @Test
    public void handle_ShouldReturnSuccess_WhenIdExists() {
        ProgramDeleteByIdCommand cmd = ProgramDeleteByIdCommand.builder()
                .id(1L)
                .build();

        ProgramEntity found = ProgramEntity.build(cmd.getId(),"","");

        when(programRepository.findById(any(Long.class))).thenReturn(Optional.of(found));
        doNothing().when(programRepository).deleteById(any(Long.class));

        OneOf2<Success, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists() {
        ProgramDeleteByIdCommand cmd = ProgramDeleteByIdCommand.builder()
                .id(1L)
                .build();
        when(programRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(cmd);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
    }
}
