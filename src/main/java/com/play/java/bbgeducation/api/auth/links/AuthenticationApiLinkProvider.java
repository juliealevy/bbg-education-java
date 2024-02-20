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

    @SneakyThrows
    public Link getLoginApiLink() {
        Optional<ApiLink> link = apiLinkService.get(AuthLinkRelations.LOGIN.value, getController(),
                getController().getMethod("authenticate", LoginRequest.class, HttpServletRequest.class),
                ApiLoginRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(AuthLinkRelations.LOGIN.value);
        }
        return link.get();
    }

    @SneakyThrows
    public Link getRefreshApiLink() {
        return apiLinkService.get(AuthLinkRelations.REFRESH.value, getController(),
                        getController().getMethod("refreshToken", HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(AuthLinkRelations.REFRESH.value));
    }

    @SneakyThrows
    public Link getRegisterApiLink() {
        Optional<ApiLink> link = apiLinkService.get(AuthLinkRelations.REGISTER.value, getController(),
                getController().getMethod("registerUser", RegisterRequest.class, HttpServletRequest.class),
                ApiRegisterRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(AuthLinkRelations.REGISTER.value);
        }
        return link.get();
    }

    public List<Link> getAllLinks(){
        List<Link> links = new ArrayList<>();
        links.add(getRegisterApiLink());
        links.add(getLoginApiLink());
        links.add(getRefreshApiLink());

        return links;
    }
}
