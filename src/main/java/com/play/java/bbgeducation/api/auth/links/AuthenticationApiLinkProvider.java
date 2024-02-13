package com.play.java.bbgeducation.api.auth.links;

import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.auth.AuthenticationController;
import com.play.java.bbgeducation.api.auth.LoginRequest;
import com.play.java.bbgeducation.api.auth.RegisterRequest;
import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationApiLinkProvider extends ApiLinkProviderBase<Class<AuthenticationController>> {

    public AuthenticationApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, AuthenticationController.class);
    }

    @SneakyThrows
    public Link getLoginApiLink() {
        return apiLinkService.get(AuthLinkRelations.LOGIN.value, getController(),
                        getController().getMethod("authenticate", LoginRequest.class, HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(AuthLinkRelations.LOGIN.value));
    }

    @SneakyThrows
    public Link getRefreshApiLink() {
        return apiLinkService.get(AuthLinkRelations.REFRESH.value, getController(),
                        getController().getMethod("refreshToken", HttpServletRequest.class, HttpServletResponse.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(AuthLinkRelations.REFRESH.value));
    }

    @SneakyThrows
    public Link getRegisterApiLink() {
        return apiLinkService.get(AuthLinkRelations.REGISTER.value, getController(),
                        getController().getMethod("registerUser", RegisterRequest.class, HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(AuthLinkRelations.REGISTER.value));
    }
}
