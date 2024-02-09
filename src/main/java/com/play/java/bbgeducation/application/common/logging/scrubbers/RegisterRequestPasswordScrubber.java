package com.play.java.bbgeducation.application.common.logging.scrubbers;

import com.play.java.bbgeducation.api.auth.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterRequestPasswordScrubber implements LoggingBodyScrubber<RegisterRequest>{
    @Override
    public RegisterRequest scrub(RegisterRequest body) {
        return RegisterRequest.builder()
                .email(body.getEmail())
                .password("")
                .firstName(body.getFirstName())
                .lastName(body.getLastName())
                .isAdmin(body.getIsAdmin())
                .build();
    }
}
