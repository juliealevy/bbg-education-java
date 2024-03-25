package com.play.java.bbgeducation.api.root;

import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.links.ApiLinkProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestResource
@RequestMapping("api")
public class ApiRootResource {

    @Autowired
    private List<ApiLinkProvider> linkProviders;

    @GetMapping(path = "")
    public ResponseEntity getLinks(
            HttpServletRequest httpRequest
    ) {

        EntityModel<ApiDataResponse> response = EntityModel.of(
                        ApiDataResponse.builder().version("1.0.0").build())
                .add(Link.of(httpRequest.getRequestURL().toString()).withSelfRel());

        linkProviders.forEach(linkProvider -> {
            response.add(linkProvider.getAllLinks());
        });

        return ResponseEntity.ok(response);
    }
}
