package com.play.java.bbgeducation.api.auth;


import com.play.java.bbgeducation.api.auth.links.AuthenticationLinkProvider;
import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import com.play.java.bbgeducation.application.auth.AuthenticationResult;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
@HasApiEndpoints
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationLinkProvider authLinkProvider;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationLinkProvider authenticationLinkProvider) {
        this.authenticationService = authenticationService;
        this.authLinkProvider = authenticationLinkProvider;
    }

    @PostMapping(path="/register")
    public ResponseEntity<?> registerUser(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest){

        OneOf2<Success, ValidationFailed> newUser = authenticationService.register(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getIsAdmin());

        return newUser.match(
                success -> new ResponseEntity<>(
                        EntityModel.of(new NoDataResponse())
                                .add(authLinkProvider.getSelfLink(httpRequest))
                                .add(authLinkProvider.getLoginLink()),
                        HttpStatus.CREATED),
                fail -> new ResponseEntity<>(
                        ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );
    }

    @PostMapping(path="login")
    public ResponseEntity authenticate(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        OneOf2<AuthenticationResult, ValidationFailed> loginResult =
                authenticationService.authenticate(request.getEmail(), request.getPassword());

        return loginResult.match(
                login -> {
                    return new ResponseEntity<>(
                            EntityModel.of(login)
                                    .add(authLinkProvider.getSelfLink(httpRequest))
                                    .add(authLinkProvider.getRefreshLink())
                            , HttpStatus.OK);
                },
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, fail.getErrorMessage()),
                        HttpStatus.UNAUTHORIZED)
        );
    }

    @PostMapping(path="refresh")
    public ResponseEntity refreshToken(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) throws IOException {

        return authenticationService.refreshToken(httpRequest, httpResponse)
                .match(
                        result -> ResponseEntity.ok(EntityModel.of(result)
                                .add(authLinkProvider.getSelfLink(httpRequest))),
                        fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, fail.getErrorMessage()),
                                HttpStatus.UNAUTHORIZED)
                );

    }
}
