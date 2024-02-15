package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.programs.SessionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  SessionRepository extends CrudRepository<SessionEntity, Long> {
    boolean existsByName(String name);
    Optional<SessionEntity> getByProgramIdAndId(Long programId, Long sessionId);
    List<SessionEntity> getByProgramId(Long programId);
}
