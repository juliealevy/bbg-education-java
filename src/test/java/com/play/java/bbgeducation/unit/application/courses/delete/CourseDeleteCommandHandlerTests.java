package com.play.java.bbgeducation.unit.application.courses.delete;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.courses.delete.CourseDeleteCommand;
import com.play.java.bbgeducation.application.courses.delete.CourseDeleteCommandHandler;
import com.play.java.bbgeducation.application.courses.getById.CourseGetByIdCommand;
import com.play.java.bbgeducation.application.courses.getById.CourseGetByIdCommandHandler;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseDeleteCommandHandlerTests {

    private CourseDeleteCommandHandler underTest;
    private CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    public CourseDeleteCommandHandlerTests(){
        this.underTest = new CourseDeleteCommandHandler(courseRepository);
    }

    @Test
    public void handle_ShouldReturnSuccess_WhenIdExists(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        course.setId(100L);

        CourseDeleteCommand command = CourseDeleteCommand.builder()
                .courseId(course.getId())
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).deleteById(any(Long.class));

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists(){
        CourseDeleteCommand command = CourseDeleteCommand.builder()
                .courseId(100L)
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        OneOf2<Success, NotFound> result = underTest.handle(command);

        assertThat(result.hasOption2()).isTrue();
    }
}
