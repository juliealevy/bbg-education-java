package com.play.java.bbgeducation.api.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class RegisterRequest {

    public RegisterRequest(){
        isAdmin = false;
    }
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    @Builder.Default
    private Boolean isAdmin = false;
}
