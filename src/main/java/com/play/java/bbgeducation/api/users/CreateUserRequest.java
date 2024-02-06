package com.play.java.bbgeducation.api.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    public CreateUserRequest(){
        isAdmin = false;
    }
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Builder.Default
    private Boolean isAdmin = false;
}
