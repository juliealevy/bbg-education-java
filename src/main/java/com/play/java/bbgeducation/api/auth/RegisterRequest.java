package com.play.java.bbgeducation.api.auth;

import com.play.java.bbgeducation.application.common.CloneableData;
import com.play.java.bbgeducation.application.common.logging.scrubbing.HasScrubOnLog;
import com.play.java.bbgeducation.application.common.logging.scrubbing.ScrubOnLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@HasScrubOnLog
public class RegisterRequest extends CloneableData {

    public RegisterRequest(){
        isAdmin = false;
    }
    private String email;
    @ScrubOnLog
    private String password;
    private String firstName;
    private String lastName;

    @Builder.Default
    private Boolean isAdmin = false;

    @Override
    public Object clone()  throws CloneNotSupportedException{
        return RegisterRequest.builder()
                .email(this.email)
                .password(this.password)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .isAdmin(this.isAdmin)
                .build();
    }

}
