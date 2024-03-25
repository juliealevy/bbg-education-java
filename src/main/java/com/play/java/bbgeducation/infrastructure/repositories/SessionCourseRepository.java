package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.programs.SessionEntity;
import com.play.java.bbgeducation.domain.sessioncourse.SessionCourseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionCourseRepository extends CrudRepository<SessionCourseEntity, Long> {
    Optional<SessionCourseEntity> getBySessionIdAndCourseId(Long sessionId, Long courseId);
}
