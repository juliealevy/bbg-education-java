package com.play.java.bbgeducation.api.root;

import com.play.java.bbgeducation.api.common.RestResource;
import com.play.java.bbgeducation.api.links.ApiLinkProvider;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestResource
@RequestMapping("api")
public class ApiRootResource {

    @Resource
    private ApiLinkProvider programApiLinkProvider;
    @Resource
    private ApiLinkProvider authenticationApiLinkProvider;
    @Resource
    private ApiLinkProvider programSessionApiLinkProvider;
    @Resource
    private ApiLinkProvider courseApiLinkProvider;
    @Resource
    private ApiLinkProvider userApiLinkProvider;

    @GetMapping(path = "")
    public ResponseEntity getLinks(
            HttpServletRequest httpRequest
    ) {

        EntityModel<ApiDataResponse> response = EntityModel.of(
                        ApiDataResponse.builder().version("1.0.0").build())
                .add(Link.of(httpRequest.getRequestURL().toString()).withSelfRel())
                .add(authenticationApiLinkProvider.getAllLinks())
                .add(programApiLinkProvider.getAllLinks())
                .add(programSessionApiLinkProvider.getAllLinks())
                .add(courseApiLinkProvider.getAllLinks())
                .add(userApiLinkProvider.getAllLinks());

        return ResponseEntity.ok(response);
    }
}
