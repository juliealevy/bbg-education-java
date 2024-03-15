package com.play.java.bbgeducation.application.auth;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.domain.valueobjects.password.Password;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;


public interface AuthenticationService {
    OneOf2<AuthenticationResult, ValidationFailed> authenticate(EmailAddress email, Password password);
    OneOf2<Success, ValidationFailed> register(EmailAddress email, Password password, FirstName firstName, LastName lastName, boolean isAdmin);

    OneOf2<AuthenticationResult, ValidationFailed> refreshToken(HttpServletRequest httpRequest) throws IOException;
}
