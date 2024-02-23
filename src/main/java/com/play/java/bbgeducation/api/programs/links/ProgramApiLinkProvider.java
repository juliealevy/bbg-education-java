package com.play.java.bbgeducation.api.programs.links;

import com.play.java.bbgeducation.api.links.ApiLink;
import com.play.java.bbgeducation.api.links.ApiLinkProviderBase;
import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import com.play.java.bbgeducation.api.programs.ProgramController;
import com.play.java.bbgeducation.api.programs.ProgramRequest;
import com.play.java.bbgeducation.api.links.ApiLinkService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProgramApiLinkProvider extends ApiLinkProviderBase<Class<ProgramController>> {


    protected ProgramApiLinkProvider(ApiLinkService apiLinkService) {
        super(apiLinkService, ProgramController.class);
    }

    public Link getCreateApiLink() {
        return getApiLink(ProgramLinkRelations.CREATE.value, ApiProgramRequest.getApiBody(),
                "createProgram", ProgramRequest.class, HttpServletRequest.class);
    }

    public Link getUpdateApiLink() {
        return getApiLink(ProgramLinkRelations.UPDATE.value, ApiProgramRequest.getApiBody(),
                "updateProgram", Long.class, ProgramRequest.class, HttpServletRequest.class);
    }

    public Link getDeleteApiLink() {
        return getApiLink(ProgramLinkRelations.DELETE.value, "deleteProgramById", Long.class, HttpServletRequest.class);
    }


    public Link getByIdApiLink() {
        return getApiLink(ProgramLinkRelations.GET_BY_ID.value, "getById", Long.class, HttpServletRequest.class);
    }


    public Link getAllApiLink() {
        return getApiLink(ProgramLinkRelations.GET_ALL.value, "getAll",HttpServletRequest.class);
    }


    @Override
    public List<Link> getAllLinks() {
        List<Link> links = new ArrayList<>();
        links.add(getCreateApiLink());
        links.add(getUpdateApiLink());
        links.add(getDeleteApiLink());
        links.add(getByIdApiLink());
        links.add(getAllApiLink());

        return links;
    }
}
