package com.play.java.bbgeducation.api.links;

import org.springframework.hateoas.Link;
import java.util.List;

public abstract class ApiLinkProviderBase<T extends Class<?>> {

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


}
