package com.play.java.bbgeducation.application.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResult {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}
