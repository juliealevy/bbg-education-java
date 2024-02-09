package com.play.java.bbgeducation.application.common.logging.scrubbers;

import com.play.java.bbgeducation.api.auth.LoginRequest;
import org.springframework.stereotype.Component;

@Component
public class LoginRequestPasswordScrubber implements LoggingBodyScrubber<LoginRequest> {
    @Override
    public LoginRequest scrub(LoginRequest body) {
        return LoginRequest.builder()
                .email(body.getEmail())
                .password("")
                .build();
    }
}
