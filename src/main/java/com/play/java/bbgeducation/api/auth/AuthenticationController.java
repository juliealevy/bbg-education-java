package com.play.java.bbgeducation.api.auth;


import com.play.java.bbgeducation.application.auth.AuthenticationService;
import com.play.java.bbgeducation.application.auth.LoginResponse;
import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping(path="/register")
    public ResponseEntity registerUser(
            @RequestBody RegisterRequest request){

        OneOf2<Success, ValidationFailed> newUser = authenticationService.register(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName());

        return newUser.match(
                success -> new ResponseEntity<>(HttpStatus.CREATED),
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );
    }

    @ResponseBody
    @RequestMapping("login")
    @PostMapping
    public ResponseEntity loginUser(
            @RequestBody LoginRequest request){

        OneOf2<LoginResponse, ValidationFailed> loginResult =
                authenticationService.login(request.getEmail(), request.getPassword());

        return loginResult.match(
                login -> new ResponseEntity<>(login, HttpStatus.OK),
                fail -> new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );
    }
}
