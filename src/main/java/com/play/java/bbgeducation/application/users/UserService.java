package com.play.java.bbgeducation.application.users;

import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.OneOf3;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;

import java.util.List;

public interface UserService {
    OneOf3<Success, NotFound, ValidationFailed> updateUser(Long id, FirstName firstName, LastName lastName);

    OneOf2<Success, NotFound> deleteUser(Long id);

    List<UserResult> getAll();

    OneOf2<UserResult, NotFound> getById(Long id);
    OneOf2<UserResult, NotFound> getByEmail(EmailAddress emailAddress);
}

