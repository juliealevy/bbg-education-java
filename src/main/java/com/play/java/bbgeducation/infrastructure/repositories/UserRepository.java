package com.play.java.bbgeducation.infrastructure.repositories;

import com.play.java.bbgeducation.domain.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String name);
}
