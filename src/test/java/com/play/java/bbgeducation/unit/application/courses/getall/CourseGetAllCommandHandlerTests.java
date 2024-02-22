package com.play.java.bbgeducation.unit.application.courses.getall;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.application.courses.getall.CourseGetAllCommand;
import com.play.java.bbgeducation.application.courses.getall.CourseGetAllCommandHandler;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseGetAllCommandHandlerTests {
    private CourseGetAllCommandHandler underTest;
    private CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    Mapper<CourseEntity, CourseResult> mapper;


    @Autowired
    public CourseGetAllCommandHandlerTests(Mapper<CourseEntity, CourseResult> mapper) {
        this.mapper = mapper;
        this.underTest = new CourseGetAllCommandHandler(courseRepository,mapper);
    }

    @Test
    public void handle_ShouldReturnList_WhenDataExists() {
        CourseEntity course1 = Instancio.create(CourseEntity.class);
        CourseEntity course2 = Instancio.create(CourseEntity.class);
        CourseGetAllCommand command = CourseGetAllCommand.builder().build();;

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<CourseResult> results = underTest.handle(command);

        assertThat(results.size()).isEqualTo(2);


    }

    @Test
    public void handle_CanReturnEmptyList_WhenNoData() {
        CourseGetAllCommand command = CourseGetAllCommand.builder().build();;

        when(courseRepository.findAll()).thenReturn(Collections.emptyList());

        List<CourseResult> results = underTest.handle(command);

        assertThat(results.size()).isEqualTo(0);
    }
}
