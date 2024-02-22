package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CourseRepository extends CrudRepository<CourseEntity, Long> {

    boolean existsByName(String courseName);
    @Query(value="SELECT * FROM course WHERE inactivated_date_time is null",
    nativeQuery = true)
    List<CourseEntity> getAllActiveCourses();

    @Modifying
    @Transactional
    @Query(value="UPDATE course SET inactivated_date_time = current_timestamp WHERE id = :id",
    nativeQuery = true)
    int inactivateCourse(@Param("id") Long courseId);
}
