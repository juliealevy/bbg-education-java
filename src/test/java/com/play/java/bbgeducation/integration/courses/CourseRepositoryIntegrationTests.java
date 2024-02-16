package com.play.java.bbgeducation.integration.courses;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import com.play.java.bbgeducation.infrastructure.repositories.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CourseRepositoryIntegrationTests {

    private final CourseRepository underTest;

    @Autowired
    public CourseRepositoryIntegrationTests(CourseRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void createCourse_ShouldSuccessAndFetch_WhenInputValid(){
        CourseEntity courseEntity = buildCourseEntity(0);
        CourseEntity saved = underTest.save(courseEntity);

        Optional<CourseEntity> fetched = underTest.findById(saved.getId());

        assertThat(fetched).isPresent();
        assertThat(fetched.get()).isEqualTo(saved);
    }

    @Test
    public void createMany_ShouldSucceedAndFetch_WhenInputValid(){
        List<CourseEntity> courseList = new ArrayList<>();
        courseList.add(buildCourseEntity(1));
        courseList.add(buildCourseEntity(2));

        Iterable<CourseEntity> savedCourses = underTest.saveAll(courseList);
        Iterable<CourseEntity> fetchedAll = underTest.findAll();

        assertThat(StreamSupport.stream(savedCourses.spliterator(),false).count()).isEqualTo(2);
        assertThat(StreamSupport.stream(fetchedAll.spliterator(),false).count()).isEqualTo(2);
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
