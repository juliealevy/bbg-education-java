package com.play.java.bbgeducation.api.links;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;

public abstract class LinkProviderBase<T extends Class<?>> {

    private T controller;
    protected T getController(){
        return controller;
    }



    protected LinkProviderBase(T controllerClass) {
        controller = controllerClass;
    }

    public Link getSelfLink(HttpServletRequest request){
        return Link.of(request.getRequestURL().toString()).withSelfRel();
    }
}
