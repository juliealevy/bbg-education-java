package com.play.java.bbgeducation.api.sessions.links;

import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.api.links.ApiLink;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import com.play.java.bbgeducation.api.sessions.ProgramSessionController;
import com.play.java.bbgeducation.api.sessions.SessionRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProgramSessionApiLinkProvider extends ApiLinkProviderBase<Class<ProgramSessionController>> {
    protected ProgramSessionApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, ProgramSessionController.class);
    }

    @SneakyThrows
    public Link getCreateApiLink() {
        Optional<ApiLink> link = apiLinkService.get(SessionLinkRelations.CREATE.value, getController(),
                getController().getMethod("createSession", Long.class, SessionRequest.class, HttpServletRequest.class),
                ApiSessionRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(SessionLinkRelations.CREATE.value);
        }
        return link.get();
    }

    @SneakyThrows
    public Link getByIdApiLink() {
        Optional<ApiLink> link = apiLinkService.get(SessionLinkRelations.GET_BY_ID.value, getController(),
                getController().getMethod("getById", Long.class, Long.class),
                ApiSessionRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(SessionLinkRelations.CREATE.value);
        }
        return link.get();
    }


    @Override
    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        links.add(getCreateApiLink());
        links.add(getByIdApiLink());
        return links;
    }
}
