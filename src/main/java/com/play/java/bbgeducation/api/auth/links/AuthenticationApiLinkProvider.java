package com.play.java.bbgeducation.api.auth.links;

import com.play.java.bbgeducation.api.auth.AuthenticationController;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.api.links.ApiLink;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Link> getAllLinks(){
        List<Link> links = new ArrayList<>();
        links.add(getRegisterApiLink());
        links.add(getLoginApiLink());
        links.add(getRefreshApiLink());

        return links;
    }
}
