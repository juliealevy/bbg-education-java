package com.play.java.bbgeducation.integration.api.users;

import com.play.java.bbgeducation.api.users.UpdateUserRequest;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.domain.valueobjects.password.Password;

public class DataUtils {

    public static UserEntity buildUserEntity(int index){
        return UserEntity.create(
                FirstName.from("testFirst_"+index),
                LastName.from("testLast_"+index),
                EmailAddress.from("testemail_"+index+"@test.com"),
                Password.from("123456_"+index), false);
    }

    public static UpdateUserRequest buildUpdateUserRequest(){
        return UpdateUserRequest.builder()
                .firstName("Julie")
                .lastName("Levy")
                .email("julie@testmail.com")
                .build();
    }

}
