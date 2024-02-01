package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;

import java.util.List;

public interface UserService {
    //passing in params to avoid controller knowing about entities and service knowing about requests...for now
    OneOf2<UserResult, ValidationFailed> createUser(String firstName, String lastName, String email, String password);
    OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, String firstName, String lastName, String email, String password);
    OneOf2<Success, NotFound> deleteUser(Long id);
    List<UserResult> getAll();
    OneOf2<UserResult, NotFound> getById(Long id);
    OneOf2<UserResult, NotFound> getByEmail(String email);
    boolean emailExists(String email);
}

