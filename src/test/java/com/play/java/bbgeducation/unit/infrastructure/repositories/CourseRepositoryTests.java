package com.play.java.bbgeducation.unit.infrastructure.repositories;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseRepositoryTests {

    private final CourseRepository underTest;

    @Autowired
    public CourseRepositoryTests(CourseRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void existsByName_ShouldReturnTrue_WhenNameExists(){
        CourseEntity courseEntity = buildCourseEntity(0);
        CourseEntity saved = underTest.save(courseEntity);

        boolean result = underTest.existsByName(saved.getName());

        assertThat(result).isTrue();
    }

    @Test
    public void existsByName_ShouldReturnFalse_WhenNameNotExists(){

        boolean result = underTest.existsByName("Test");

        assertThat(result).isFalse();
    }

    private CourseEntity buildCourseEntity(int count){
        return CourseEntity.builder()
                .name("Test course" + count)
                .description("Test description")
                .isPublic(true)
                .isOnline(false)
                .build();
    }
}
