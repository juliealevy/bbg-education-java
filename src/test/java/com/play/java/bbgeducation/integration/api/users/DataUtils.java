package com.play.java.bbgeducation.integration.api.users;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;

public class DataUtils {

    public static UserEntity buildUserEntity(int index){
        return UserEntity.create(
                "testFirst_"+index,
                "testLast_"+index,
                EmailAddress.from("testemail_"+index+"@test.com"),
                "123456_"+index, false);
    }

    public static UpdateUserRequest buildUpdateUserRequest(){
        return UpdateUserRequest.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .build();
    }

}
