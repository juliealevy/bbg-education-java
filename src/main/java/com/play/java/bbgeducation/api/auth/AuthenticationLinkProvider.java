package com.play.java.bbgeducation.api.auth;

import com.play.java.bbgeducation.api.LinkProviderBase;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthenticationLinkProvider extends LinkProviderBase {

    public Link getLoginLink() {
        return linkTo(methodOn(AuthenticationController.class)
                .authenticate(LoginRequest.builder().build(), null)).withRel("auth:login");
    }

    @SneakyThrows
    public Link getRefreshLink()  {
        return linkTo(methodOn(AuthenticationController.class)
                .refreshToken(null, null)).withRel("auth:refresh");
    }

    public Link getRegisterLink(boolean asSelf){
        return linkTo(methodOn(AuthenticationController.class)
                .registerUser(RegisterRequest.builder().build(),null)).withRel("auth:register");
    }
}
