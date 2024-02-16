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

    @SneakyThrows
    public Link getCreateApiLink() {
        Optional<ApiLink> link = apiLinkService.get(ProgramLinkRelations.CREATE.value, getController(),
                getController().getMethod("createProgram", ProgramRequest.class, HttpServletRequest.class),
                ApiProgramRequest.getApiBody());

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(ProgramLinkRelations.CREATE.value);
        }
        return link.get();
    }

    @SneakyThrows
    public Link getUpdateApiLink() {
        Optional<ApiLink> link = apiLinkService.get(ProgramLinkRelations.UPDATE.value, getController(),
                getController().getMethod("updateProgram", Long.class, ProgramRequest.class, HttpServletRequest.class),
                ApiProgramRequest.getApiBody());

        if (link.isEmpty()) {
            throw new InvalidApiEndpointLinkException(ProgramLinkRelations.CREATE.value);
        }
        return link.get();
    }

    @SneakyThrows
    public Link getDeleteApiLink() {
        return apiLinkService.get(ProgramLinkRelations.DELETE.value, getController(),
                        getController().getMethod("deleteProgramById", Long.class, HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(ProgramLinkRelations.DELETE.value));

    }


    @SneakyThrows
    public Link getByIdApiLink() {
        return apiLinkService.get(ProgramLinkRelations.GET_BY_ID.value, getController(),
                        getController().getMethod("getById", Long.class, HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(ProgramLinkRelations.GET_BY_ID.value));

    }


    @SneakyThrows
    public Link getAllApiLink() {
        return apiLinkService.get(ProgramLinkRelations.GET_ALL.value, getController(),
                        getController().getMethod("getAll",HttpServletRequest.class))
                .orElseThrow(() -> new InvalidApiEndpointLinkException(ProgramLinkRelations.GET_ALL.value));

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