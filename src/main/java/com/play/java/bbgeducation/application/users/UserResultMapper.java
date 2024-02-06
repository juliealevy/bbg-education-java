package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.users.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserResultMapper implements Mapper<UserEntity, UserResult> {
    @Override
    public UserResult mapTo(UserEntity userEntity) {
        return UserResult.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .isAdmin(userEntity.getIsAdmin())
                .build();

    }

    @Override
    public UserEntity mapFrom(UserResult userResult) {
        return null;
    }
}
