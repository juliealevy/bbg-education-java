package com.play.java.bbgeducation.api.auth.links;

import com.play.java.bbgeducation.api.links.LinkProviderBase;
import com.play.java.bbgeducation.api.auth.AuthenticationController;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthenticationLinkProvider extends LinkProviderBase<Class<AuthenticationController>> {
    protected AuthenticationLinkProvider() {
        super(AuthenticationController.class);
    }

    public Link getLoginLink() {
        return linkTo(methodOn(getController())
                .authenticate(LoginRequest.builder().build(), null)).withRel(AuthLinkRelations.LOGIN.value);
    }



    @SneakyThrows
    public Link getRefreshLink()  {
        return linkTo(methodOn(getController())
                .refreshToken(null, null)).withRel(AuthLinkRelations.REFRESH.value);
    }



    public Link getRegisterLink(boolean asSelf){
        return linkTo(methodOn(getController())
                .registerUser(RegisterRequest.builder().build(),null)).withRel(AuthLinkRelations.REGISTER.value);
    }


}