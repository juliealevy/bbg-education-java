package com.play.java.bbgeducation.api.auth;


import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import com.play.java.bbgeducation.application.auth.LoginResult;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationLinkProvider authLinkProvider;

    public AuthenticationController(AuthenticationService authenticationService, AuthenticationLinkProvider authenticationLinkProvider) {
        this.authenticationService = authenticationService;
        this.authLinkProvider = authenticationLinkProvider;
    }

    @PostMapping(path="/register")
    public ResponseEntity registerUser(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest){

        OneOf2<Success, ValidationFailed> newUser = authenticationService.register(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getIsAdmin());

        return newUser.match(
                success -> {
                    return new ResponseEntity<>(
                            EntityModel.of(new NoDataResponse())
                                    .add(authLinkProvider.getSelfLink(httpRequest))
                                    .add(authLinkProvider.getLoginLink()),
                            HttpStatus.CREATED);
                },
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );
    }

    @ResponseBody
    @RequestMapping("login")
    @PostMapping
    public ResponseEntity loginUser(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest){

        OneOf2<LoginResult, ValidationFailed> loginResult =
                authenticationService.login(request.getEmail(), request.getPassword());

        return loginResult.match(
                login -> {
                    return new ResponseEntity<>(
                            EntityModel.of(login)
                            .add(authLinkProvider.getSelfLink(httpRequest)), HttpStatus.OK);
                },
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );
    }
}
