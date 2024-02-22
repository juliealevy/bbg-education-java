package com.play.java.bbgeducation.unit.application.courses.update;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.NameExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.courses.results.CourseResult;
import com.play.java.bbgeducation.application.courses.update.CourseUpdateCommand;
import com.play.java.bbgeducation.application.courses.update.CourseUpdateCommandHandler;
import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseUpdateCommandHandlerTests {
    private CourseUpdateCommandHandler underTest;
    Mapper<CourseEntity, CourseResult> mapper;
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);

    @BeforeEach
    void setUp() {
        underTest = new CourseUpdateCommandHandler(courseRepository);
    }

    @Test
    public void handle_ShouldSucceed_WhenInputValid(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseUpdateCommand command = CourseUpdateCommand.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription() + " Updated")
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(course);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    public void handle_ShouldSucceed_WhenNameChangesAndNotExists(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseUpdateCommand command = CourseUpdateCommand.builder()
                .id(course.getId())
                .name(course.getName() + " updated")
                .description(course.getDescription())
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseRepository.existsByName(any(String.class))).thenReturn(false);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption1()).isTrue();
    }

    @Test
    public void handle_ShouldFail_WhenNameChangesAndExists(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseUpdateCommand command = CourseUpdateCommand.builder()
                .id(course.getId())
                .name(course.getName() + " updated")
                .description(course.getDescription())
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(course));
        when(courseRepository.existsByName(any(String.class))).thenReturn(true);

        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption3()).isTrue();
        assertThat(result.asOption3() instanceof NameExistsValidationFailed).isTrue();
    }

    @Test
    public void handle_ShouldReturnNotFound_WhenIdNotExists(){
        CourseEntity course = Instancio.create(CourseEntity.class);
        CourseUpdateCommand command = CourseUpdateCommand.builder()
                .id(course.getId())
                .name(course.getName() + " updated")
                .description(course.getDescription())
                .isOnline(course.getIsOnline())
                .isPublic(course.getIsPublic())
                .build();

        when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        OneOf3<Success, NotFound, ValidationFailed> result = underTest.handle(command);

        assertThat(result).isNotNull();
        assertThat(result.hasOption2()).isTrue();

    }

}
