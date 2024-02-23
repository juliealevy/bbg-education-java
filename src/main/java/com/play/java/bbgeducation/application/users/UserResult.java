package com.play.java.bbgeducation.application.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@AllArgsConstructor
@Builder
@Relation(collectionRelation="users")
public class UserResult {

    public UserResult(){
        isAdmin = false;
    }

    private Long id;
    private String email;
    private String firstName;
    private String lastName;

    @Builder.Default
    private boolean isAdmin = false;
}
