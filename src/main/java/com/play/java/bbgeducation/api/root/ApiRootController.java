package com.play.java.bbgeducation.api.root;

import com.play.java.bbgeducation.api.auth.links.AuthenticationApiLinkProvider;
import com.play.java.bbgeducation.api.programs.links.ProgramApiLinkProvider;
import com.play.java.bbgeducation.api.sessions.ProgramSessionController;
import com.play.java.bbgeducation.api.sessions.links.ProgramSessionApiLinkProvider;
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
    private final ProgramSessionApiLinkProvider programSessionApiLinkProvider;

    public ApiRootController(ProgramApiLinkProvider programApiLinkProvider, AuthenticationApiLinkProvider authApiLinkProvider, ProgramSessionApiLinkProvider programSessionApiLinkProvider) {
        this.programApiLinkProvider = programApiLinkProvider;
        this.authApiLinkProvider = authApiLinkProvider;
        this.programSessionApiLinkProvider = programSessionApiLinkProvider;
    }


    @SneakyThrows
    @GetMapping(path = "")
    public ResponseEntity getLinks(
            HttpServletRequest httpRequest
    ) {

        EntityModel<ApiDataResponse> response = EntityModel.of(
                        ApiDataResponse.builder().version("1.0.0").build())
                .add(Link.of(httpRequest.getRequestURI()).withSelfRel())
                .add(authApiLinkProvider.getAllLinks())
                .add(programApiLinkProvider.getAllLinks())
                .add(programSessionApiLinkProvider.getAllLinks());


        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
