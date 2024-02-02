package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.programs.ProgramEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends CrudRepository<ProgramEntity, Long> {

    Optional<ProgramEntity> findByName(String name);

    boolean existsByName(String name);


}
