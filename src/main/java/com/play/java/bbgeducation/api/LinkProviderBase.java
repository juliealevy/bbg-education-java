package com.play.java.bbgeducation.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.Link;

public abstract class LinkProviderBase {

    public Link getSelfLink(HttpServletRequest request){
        return Link.of(request.getRequestURI()).withSelfRel();
    }
}
