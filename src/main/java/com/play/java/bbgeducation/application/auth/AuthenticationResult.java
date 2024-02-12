package com.play.java.bbgeducation.application.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.play.java.bbgeducation.application.common.CloneableData;
import com.play.java.bbgeducation.application.common.logging.scrubbing.HasScrubOnLog;
import com.play.java.bbgeducation.application.common.logging.scrubbing.ScrubOnLog;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@HasScrubOnLog
public class AuthenticationResult extends CloneableData {

    @JsonProperty("access_token")
    @ScrubOnLog
    private String accessToken;

    @JsonProperty("refresh_token")
    @ScrubOnLog
    private String refreshToken;


    @Override
    public Object clone() throws CloneNotSupportedException {
        return AuthenticationResult.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
    }
}
