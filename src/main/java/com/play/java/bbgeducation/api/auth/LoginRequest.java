package com.play.java.bbgeducation.api.auth;

import com.play.java.bbgeducation.application.common.CloneableData;
import com.play.java.bbgeducation.application.common.logging.scrubbing.HasScrubOnLog;
import com.play.java.bbgeducation.application.common.logging.scrubbing.ScrubOnLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HasScrubOnLog
public class LoginRequest extends CloneableData {
    private String email;
    @ScrubOnLog
    private String password;

    @Override
    public Object clone() throws CloneNotSupportedException{
        return LoginRequest.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }

}
