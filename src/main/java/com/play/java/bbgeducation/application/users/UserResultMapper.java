package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.mapping.Mapper;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import org.springframework.stereotype.Component;

@Component
public class UserResultMapper implements Mapper<UserEntity, UserResult> {
    @Override
    public UserResult mapTo(UserEntity userEntity) {
        return UserResult.builder()
                .id(userEntity.getId())
                .firstName(userEntity.getFirstName().toString())
                .lastName(userEntity.getLastName().toString())
                .email(userEntity.getUsername())
                .isAdmin(userEntity.getIsAdmin())
                .build();

    }

    @Override
    public UserEntity mapFrom(UserResult userResult) {
        return null;
    }
}
