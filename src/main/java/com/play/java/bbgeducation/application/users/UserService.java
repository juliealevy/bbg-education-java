package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;

import java.util.List;

public interface UserService {
    OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, String firstName, String lastName, String email);

    OneOf2<Success, NotFound> deleteUser(Long id);

    List<UserResult> getAll();

    OneOf2<UserResult, NotFound> getById(Long id);
}

