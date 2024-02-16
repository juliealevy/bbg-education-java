package com.play.java.bbgeducation.unit.courses.commands.create;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommand;
import com.play.java.bbgeducation.application.courses.create.CourseCreateCommandHandler;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.courses.results.CourseResultMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseCreateCommandHandlerTests {
    private CourseCreateCommandHandler underTest;
    Mapper<CourseEntity, CourseResult> mapper;
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);


    @Autowired
    public CourseCreateCommandHandlerTests(Mapper<CourseEntity, CourseResult> mapper) {
        this.mapper = mapper;
        underTest = new CourseCreateCommandHandler(courseRepository, mapper);
    }

    @Test
    public void handle_ShouldCreateCourse_WhenInputValid(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseCreateCommand command = CourseCreateCommand.builder()
                .name(course.getName())
                .description(course.getDescription())
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.existsByName(any(String.class))).thenReturn(false);
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(course);

        OneOf2<CourseResult, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1()).isEqualTo(mapper.mapTo(course));

    }

    @Test
    public void handle_ShouldFail_WhenNameExists(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseCreateCommand command = CourseCreateCommand.builder()
                .name(course.getName())
                .description(course.getDescription())
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.existsByName(any(String.class))).thenReturn(true);

        OneOf2<CourseResult, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();
        assertThat(result.asOption2() instanceof NameExistsValidationFailed).isTrue();
    }

}
