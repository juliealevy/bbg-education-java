package com.play.java.bbgeducation.api.links;

import org.springframework.hateoas.Link;

import java.util.List;

public interface ApiLinkProvider {
    List<Link> getAllLinks();
}
