package com.play.java.bbgeducation.application.auth;

import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;


public interface AuthenticationService {
    OneOf2<LoginResult, ValidationFailed> login(String email, String password);
    OneOf2<Success, ValidationFailed> register(String email, String password, String firstName, String lastName, boolean isAdmin);
}
