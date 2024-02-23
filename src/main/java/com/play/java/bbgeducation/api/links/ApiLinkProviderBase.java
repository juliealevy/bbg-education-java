package com.play.java.bbgeducation.api.links;

import com.play.java.bbgeducation.api.courses.links.CourseLinkRelations;
import com.play.java.bbgeducation.api.endpoints.InvalidApiEndpointLinkException;
import lombok.SneakyThrows;
import org.springframework.hateoas.Link;
import java.util.List;
import java.util.Optional;

public abstract class ApiLinkProviderBase<T extends Class<?>> implements ApiLinkProvider {

    private T controller;
    protected final ApiLinkService apiLinkService;

    protected ApiLinkProviderBase(ApiLinkService apiLinkService, T controllerClass) {
        this.apiLinkService = apiLinkService;
        this.controller = controllerClass;
    }

    protected T getController(){
        return controller;
    }

    public abstract List<Link> getAllLinks();

    @SneakyThrows
    protected Link getApiLink(String linkRelation, String methodName, Class<?>... methodParameterTypes){
        return getApiLink(linkRelation, null, methodName, methodParameterTypes);
    }

    @SneakyThrows
    protected Link getApiLink(String linkRelation, Object requestBody, String methodName, Class<?>... methodParameterTypes){
        Optional<ApiLink> link = apiLinkService.get(linkRelation, getController(),
                getController().getMethod(methodName, methodParameterTypes), requestBody);

        if (link.isEmpty()){
            throw new InvalidApiEndpointLinkException(CourseLinkRelations.DELETE.value);
        }
        return link.get();
    }


}
