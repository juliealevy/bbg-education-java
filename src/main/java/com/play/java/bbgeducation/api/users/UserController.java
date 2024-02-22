package com.play.java.bbgeducation.api.users;

import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@HasApiEndpoints
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(path="{id}")
    public ResponseEntity updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequest userRequest) {

        OneOf3<Success, NotFound, ValidationFailed> updated = userService.updateUser(
                id,
                userRequest.getFirstName(),
                userRequest.getLastName(),
                userRequest.getEmail(),
                userRequest.getPassword());

        return updated.match(
                success -> ResponseEntity.ok().build(),
                notfound -> ResponseEntity.notFound().build(),
                fail ->  ResponseEntity.of(fail.toProblemDetail("Error updating user"))
                        .build()
        );
    }

    @GetMapping(path="")
    public ResponseEntity<List<UserResult>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @GetMapping(path="{id}")
    public ResponseEntity getById(
            @PathVariable("id") Long id
    ) {
        OneOf2<UserResult, NotFound> result = userService.getById(id);

        //TODO:  need links
        return result.match(
                user -> ResponseEntity.ok(user),
                notfound ->  ResponseEntity.notFound().build()
        );

    }

    @DeleteMapping(path="{id}")
    public ResponseEntity deleteById(
            @PathVariable("id") Long id
    ) {
        OneOf2<Success, NotFound> result = userService.deleteUser(id);

        //TODO:  need links
        return result.match(
                success -> ResponseEntity.noContent().build(),
                notfound ->  ResponseEntity.notFound().build()
        );

    }
}
