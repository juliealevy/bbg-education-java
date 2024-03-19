package com.play.java.bbgeducation.application.auth;

import an.awesome.pipelinr.repack.com.google.common.base.Strings;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.domain.valueobjects.password.Password;
import com.play.java.bbgeducation.infrastructure.auth.AuthHeaderParser;
import com.play.java.bbgeducation.infrastructure.auth.JwtService;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private AuthHeaderParser authHeaderParser;
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, AuthHeaderParser authHeaderParser, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.authHeaderParser = authHeaderParser;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OneOf2<Success, ValidationFailed> register(EmailAddress email, Password password, FirstName firstName, LastName lastName, boolean isAdmin) {

        if (userRepository.existsByEmail(email)) {
            return OneOf2.fromOption2(new EmailExistsValidationFailed());
        }

        UserEntity userEntity = UserEntity.create(
                firstName, lastName, email,
                Password.from(passwordEncoder.encode(password.toString())), isAdmin);

        try {
            UserEntity saved = userRepository.save(userEntity);
        } catch (TransactionSystemException cvex) {
            logger.error("Error updating user", cvex);
            Throwable mostSpecificCause = cvex.getMostSpecificCause();
            return OneOf2.fromOption2(ValidationFailed.Conflict("", mostSpecificCause.getMessage()));
        }

        //for now, not returning any tokens.  forcing the login call
        return OneOf2.fromOption1(new Success());

    }


    @SneakyThrows
    @Override
    public OneOf2<AuthenticationResult, ValidationFailed> authenticate(EmailAddress email, Password password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (InternalAuthenticationServiceException ex) {
            if (ex.getCause() instanceof BadCredentialsException) {
                throw new BadCredentialsException(ex.getMessage());
            }
        }

        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            //don't want to return info to the user whether email or password was the problem
            return OneOf2.fromOption2(ValidationFailed.Unauthorized("", "Bad Credentials"));
        }

        return OneOf2.fromOption1(buildAuthenticationResult(user.get()));
    }

    @Override
    public OneOf2<AuthenticationResult, ValidationFailed> refreshToken(HttpServletRequest request) {

        final String refreshToken = authHeaderParser.getAuthToken(request);
        OneOf2<EmailAddress, ValidationFailed> validatedRequest = validateRequest(request);
        if (validatedRequest.hasOption2()){
            return OneOf2.fromOption2(validatedRequest.asOption2());
        }
        EmailAddress currentEmail = validatedRequest.asOption1();

        UserEntity userDetails = this.userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        AuthenticationResult authResponse = AuthenticationResult.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();

        return OneOf2.fromOption1(authResponse);

    }

    public OneOf2<AuthenticationResult, ValidationFailed> updateUserName(HttpServletRequest request, EmailAddress newUserName) {

        OneOf2<EmailAddress, ValidationFailed> validatedRequest = validateRequest(request);
        if (validatedRequest.hasOption2()){
            return OneOf2.fromOption2(validatedRequest.asOption2());
        }

        try {
            EmailAddress currentUserEmail = validatedRequest.asOption1();
            UserEntity userDetails = this.userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            //validate new username
            if (newUserName.isEmpty()){
                return OneOf2.fromOption2(ValidationFailed.Conflict("email", "New email address must have a value"));
            }
            if (!newUserName.equals(currentUserEmail) && userRepository.findByEmail(newUserName).isPresent()){
                return OneOf2.fromOption2(new EmailExistsValidationFailed());
            }
            //save it
            userDetails.setEmail(newUserName);
            userRepository.save(userDetails);
            //Todo: revoke token?  (requires persisting tokens in DB)
            return OneOf2.fromOption1(buildAuthenticationResult(userDetails));
        } catch (TransactionSystemException ex) {
            logger.error("Error updating username/email", ex);
            Throwable mostSpecificCause = ex.getMostSpecificCause();
            return OneOf2.fromOption2(ValidationFailed.Conflict("", mostSpecificCause.getMessage()));
        }



    }

    @Override
    public OneOf2<Success, ValidationFailed> updatePassword(HttpServletRequest request, Password oldPassword, Password newPassword) {

        OneOf2<EmailAddress, ValidationFailed> validatedRequest = validateRequest(request);
        if (validatedRequest.hasOption2()){
            return OneOf2.fromOption2(validatedRequest.asOption2());
        }

        try {
            EmailAddress currentUserEmail = validatedRequest.asOption1();
            UserEntity userDetails = this.userRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

            if (!passwordEncoder.matches(oldPassword.toString(), userDetails.getPassword())) {
                return OneOf2.fromOption2(ValidationFailed.Conflict("password", "Existing password is incorrect"));
            }
            userDetails.setPassword(Password.from(passwordEncoder.encode(newPassword.toString())));
            userRepository.save(userDetails);
            //TODO:  revoke token??  (requires persisting tokens to DB)
        }catch(TransactionSystemException ex){
            logger.error("Error updating user password", ex);
            Throwable mostSpecificCause = ex.getMostSpecificCause();
            return OneOf2.fromOption2(ValidationFailed.Conflict("", mostSpecificCause.getMessage()));
        }
        return OneOf2.fromOption1(new Success());
    }

    private OneOf2<EmailAddress, ValidationFailed> validateRequest(HttpServletRequest request){
        final String token = authHeaderParser.getAuthToken(request);
        if (Strings.isNullOrEmpty(token)) {
            return OneOf2.fromOption2(ValidationFailed.Unauthorized("", "Invalid or no authorization provided"));
        }
        if (jwtService.isTokenExpired(token)) {
            return OneOf2.fromOption2(ValidationFailed.Unauthorized("", "Authorization is expired"));
        }

        final String userEmail = jwtService.extractUserName(token);
        if (Strings.isNullOrEmpty(userEmail)) {
            return OneOf2.fromOption2(ValidationFailed.Unauthorized("", "Invalid authorization provided"));
        }
        return OneOf2.fromOption1(EmailAddress.from(userEmail));
    }

    private AuthenticationResult buildAuthenticationResult(UserEntity user){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("authorities", user.getAuthorities());
        extraClaims.put("firstName", user.getFirstName().toString());
        extraClaims.put("lastName", user.getLastName().toString());
        String accessToken = jwtService.generateAccessToken(extraClaims, user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResult.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}




