package com.play.java.bbgeducation.api.root;

import com.play.java.bbgeducation.api.auth.links.AuthenticationApiLinkProvider;
import com.play.java.bbgeducation.api.programs.links.ProgramApiLinkProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class ApiRootController {

    private final ProgramApiLinkProvider programApiLinkProvider;
    private final AuthenticationApiLinkProvider authApiLinkProvider;

    public ApiRootController(ProgramApiLinkProvider programApiLinkProvider, AuthenticationApiLinkProvider authApiLinkProvider) {
        this.programApiLinkProvider = programApiLinkProvider;
        this.authApiLinkProvider = authApiLinkProvider;
    }


    @SneakyThrows
    @GetMapping(path = "")
    public ResponseEntity getLinks(
            HttpServletRequest httpRequest
    ) {

        EntityModel<ApiDataResponse> response = EntityModel.of(
                        ApiDataResponse.builder().version("1.0.0").build())
                .add(Link.of(httpRequest.getRequestURI()).withSelfRel());

        response
                .add(authApiLinkProvider.getRegisterApiLink())
                .add(authApiLinkProvider.getLoginApiLink())
                .add(authApiLinkProvider.getRefreshApiLink());

        response
                .add(programApiLinkProvider.getCreateApiLink())
                .add(programApiLinkProvider.getUpdateApiLink())
                .add(programApiLinkProvider.getDeleteApiLink())
                .add(programApiLinkProvider.getByIdApiLink())
                .add(programApiLinkProvider.getAllApiLink());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
