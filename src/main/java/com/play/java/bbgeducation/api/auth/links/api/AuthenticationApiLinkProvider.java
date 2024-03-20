package com.play.java.bbgeducation.api.auth.links.api;

import com.play.java.bbgeducation.api.auth.*;
import com.play.java.bbgeducation.api.auth.links.AuthLinkRelations;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationApiLinkProvider extends ApiLinkProviderBase<Class<AuthenticationController>> {

    public AuthenticationApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, AuthenticationController.class);
    }

    public Link getLoginApiLink() {
        return getApiLink(AuthLinkRelations.LOGIN.value,ApiLoginRequest.getApiBody(),
                "authenticate", LoginRequest.class, HttpServletRequest.class);
    }

    public Link getRefreshApiLink() {
        return getApiLink(AuthLinkRelations.REFRESH.value,"refreshToken", HttpServletRequest.class);
    }

    public Link getRegisterApiLink() {
        return getApiLink(AuthLinkRelations.REGISTER.value, ApiRegisterRequest.getApiBody(),
                "registerUser", RegisterRequest.class, HttpServletRequest.class);
    }

    public Link getUpdateUserNameApiLink() {
        return getApiLink(AuthLinkRelations.UPDATE_USERNAME.value, ApiUpdateUserNameRequest.getApiBody(),
                "updateUserName", UpdateUserNameRequest.class, HttpServletRequest.class);
    }

    public Link getUpdatePasswordApiLink() {
        return getApiLink(AuthLinkRelations.UPDATE_PASSWORD.value, ApiUpdatePasswordRequest.getApiBody(),
                "updatePassword", UpdatePasswordRequest.class, HttpServletRequest.class);
    }

    public List<Link> getAllLinks(){
        List<Link> links = new ArrayList<>();
        links.add(getRegisterApiLink());
        links.add(getLoginApiLink());
        links.add(getRefreshApiLink());
        links.add(getUpdateUserNameApiLink());
        links.add(getUpdatePasswordApiLink());

        return links;
    }
}
