package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.courses.CourseEntity;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<CourseEntity, Long> {

    boolean existsByName(String courseName);
}
