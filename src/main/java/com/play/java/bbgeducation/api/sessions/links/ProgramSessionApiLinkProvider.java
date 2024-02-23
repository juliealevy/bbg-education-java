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
        return getApiLink(SessionLinkRelations.CREATE.value, ApiSessionRequest.getApiBody(),
                "createSession", Long.class, SessionRequest.class, HttpServletRequest.class);
    }

    @SneakyThrows
    public Link getByIdApiLink() {
        return getApiLink(SessionLinkRelations.GET_BY_ID.value, "getById", Long.class, Long.class,HttpServletRequest.class);
    }

    @SneakyThrows
    public Link getByProgramApiLink() {
        return getApiLink(SessionLinkRelations.GET_BY_PROGRAM.value, "getByProgram",Long.class, HttpServletRequest.class);
    }

    @SneakyThrows
    public Link getDeleteApiLink() {
        return getApiLink(SessionLinkRelations.DELETE.value, "deleteSession",Long.class, Long.class, HttpServletRequest.class);
    }

    @SneakyThrows
    public Link getUpdateApiLink() {
        return getApiLink(SessionLinkRelations.UPDATE.value, ApiSessionRequest.getApiBody(),
                "updateSession", Long.class, Long.class, SessionRequest.class, HttpServletRequest.class);
    }

    @Override
    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        links.add(getCreateApiLink());
        links.add(getByIdApiLink());
        links.add(getByProgramApiLink());
        links.add(getDeleteApiLink());
        links.add(getUpdateApiLink());
        return links;
    }
}
