package com.play.java.bbgeducation.api.users;

import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path="")
    public ResponseEntity createUser(
            @RequestBody UserRequest userRequest) {

        OneOf2<UserResult, ValidationFailed> created = userService.createUser(userRequest.getFirstName(), userRequest.getLastName(),
                userRequest.getEmail(), userRequest.getPassword());

        return created.match(
                user -> new ResponseEntity<>(user, HttpStatus.CREATED),
                fail ->  new ResponseEntity<>(ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, fail.getErrorMessage()),
                        HttpStatus.CONFLICT)
        );

    }
}
