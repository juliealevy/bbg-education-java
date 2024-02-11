package com.play.java.bbgeducation.application.auth;

import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface AuthenticationService {
    OneOf2<AuthenticationResult, ValidationFailed> authenticate(String email, String password);
    OneOf2<Success, ValidationFailed> register(String email, String password, String firstName, String lastName, boolean isAdmin);

    OneOf2<AuthenticationResult, ValidationFailed> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException;
}
