package com.play.java.bbgeducation.api.users;

import com.play.java.bbgeducation.api.common.NoDataResponse;
import com.play.java.bbgeducation.api.endpoints.HasApiEndpoints;
import com.play.java.bbgeducation.api.users.links.UserLinkProvider;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@HasApiEndpoints
public class UserController {
    private final UserService userService;
    private final UserLinkProvider userLinkProvider;

    public UserController(UserService userService, UserLinkProvider userLinkProvider) {
        this.userService = userService;
        this.userLinkProvider = userLinkProvider;
    }

    @PutMapping(path="{id}")
    public ResponseEntity updateUser(
            @PathVariable("id") Long id,
            @RequestBody UpdateUserRequest userRequest,
            HttpServletRequest httpRequest) {

        OneOf3<Success, NotFound, ValidationFailed> updated = userService.updateUser(
                id,
                FirstName.from(userRequest.getFirstName()),
                LastName.from(userRequest.getLastName()));

        return updated.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(userLinkProvider.getSelfLink(httpRequest))
                        .add(userLinkProvider.getByIdLink(id, false))),
                notfound -> ResponseEntity.notFound().build(),
                fail ->  ResponseEntity.of(fail.toProblemDetail("Error updating user"))
                        .build()
        );
    }

    @GetMapping(path="")
    public ResponseEntity getAll(
            HttpServletRequest httpRequest
    ) {
        return ResponseEntity.ok(buildCollectionModel(userService.getAll(),httpRequest));
    }

    @GetMapping(path="{id}")
    public ResponseEntity getById(
            @PathVariable("id") Long id
    ) {
        OneOf2<UserResult, NotFound> result = userService.getById(id);

        return result.match(
                user -> ResponseEntity.ok(buildEntityModelUserItem(user)),
                notfound ->  ResponseEntity.notFound().build()
        );

    }

    @GetMapping(path="email")
    public ResponseEntity getByEmail(
            @RequestBody GetUserByEmailRequest getByFilterRequest
    ) {
        OneOf2<UserResult, NotFound> result = userService.getByEmail(
                EmailAddress.from(getByFilterRequest.getEmail()));

        return result.match(
                user -> ResponseEntity.ok(buildEntityModelUserItem(user)),
                notfound ->  ResponseEntity.notFound().build()
        );

    }

    @DeleteMapping(path="{id}")
    public ResponseEntity deleteById(
            @PathVariable("id") Long id,
            HttpServletRequest httpRequest
    ) {
        OneOf2<Success, NotFound> result = userService.deleteUser(id);

        return result.match(
                success -> ResponseEntity.ok(EntityModel.of(new NoDataResponse())
                        .add(userLinkProvider.getSelfLink(httpRequest))
                        .add(userLinkProvider.getAllLink())),
                notfound ->  ResponseEntity.notFound().build()
        );

    }

    CollectionModel<EntityModel<UserResult>> buildCollectionModel(List<UserResult> userList, HttpServletRequest httpRequest){
        return CollectionModel.of(buildEntityModelItemList( userList))
                .add(userLinkProvider.getSelfLink(httpRequest));

    }

    List<EntityModel<UserResult>> buildEntityModelItemList(List<UserResult> userList){
        return userList.stream()
                .map(this::buildEntityModelUserItem)
                .toList();
    }
    EntityModel<UserResult> buildEntityModelUserItem(UserResult result){
        return  EntityModel.of(result)
                .add(userLinkProvider.getByIdLink(result.getId(), true))
                .add(userLinkProvider.getUpdateLink(result.getId()))
                .add(userLinkProvider.getDeleteLink(result.getId()))
                .add(userLinkProvider.getAllLink());
    }
}
