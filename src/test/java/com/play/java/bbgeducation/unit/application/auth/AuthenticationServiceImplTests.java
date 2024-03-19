package com.play.java.bbgeducation.unit.application.auth;

import com.play.java.bbgeducation.application.auth.AuthenticationResult;
import com.play.java.bbgeducation.application.auth.AuthenticationService;
import com.play.java.bbgeducation.application.auth.AuthenticationServiceImpl;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.ValidationErrorType;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.application.exceptions.InvalidEmailFormatException;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.domain.valueobjects.emailaddress.EmailAddress;
import com.play.java.bbgeducation.domain.valueobjects.firstname.FirstName;
import com.play.java.bbgeducation.domain.valueobjects.lastname.LastName;
import com.play.java.bbgeducation.domain.valueobjects.password.Password;
import com.play.java.bbgeducation.infrastructure.auth.AuthHeaderParser;
import com.play.java.bbgeducation.infrastructure.auth.JwtService;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceImplTests {


    private AuthenticationService underTest;
    private UserEntity userEntity;

    private AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);

    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private JwtService jwtService = Mockito.mock(JwtService.class);
    private AuthHeaderParser authHeaderParser = Mockito.mock(AuthHeaderParser.class);
    private PasswordEncoder passwordEncoder;
    private HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
    private final String userPassword = "123456";
    private final Password updatedPassword = Password.from("654321");
    private final EmailAddress userEmail = EmailAddress.from("test@test.com");
    private final EmailAddress updatedEmail = EmailAddress.from("blah@blah.com");
    private final String mockToken = "12345";

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        underTest = new AuthenticationServiceImpl(authenticationManager, userRepository, authHeaderParser,
                jwtService, passwordEncoder);

        userEntity = UserEntity.create(
                FirstName.from("TestFirst"),
                LastName.from("TestLast"),
                userEmail,
                Password.from(passwordEncoder.encode(userPassword)),
                true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerUser_shouldReturnSuccess_whenInputValid() {

        when(userRepository.existsByEmail(userEntity.getEmail())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        OneOf2<Success, ValidationFailed> result = underTest.register(
                userEntity.getEmail(),
                userEntity.getPasswordObject(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getIsAdmin());

        assertThat(result.hasOption1()).isTrue();
    }


    @Test
    void registerUser_shouldReturnFail_whenEmailExists() {

        when(userRepository.existsByEmail(userEntity.getEmail())).thenReturn(true);

        OneOf2<Success, ValidationFailed> result = underTest.register(
                userEntity.getEmail(), userEntity.getPasswordObject(), userEntity.getFirstName(),
                userEntity.getLastName(), userEntity.getIsAdmin());

        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    void registerUser_shouldThrow_whenEmailInvalid() {

        when(userRepository.existsByEmail(userEntity.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> underTest.register(
                EmailAddress.from(userEntity.getUsername() + "invalid"),
                userEntity.getPasswordObject(), userEntity.getFirstName(),
                userEntity.getLastName(), userEntity.getIsAdmin()))
                .isInstanceOf(InvalidEmailFormatException.class);
    }

    @Test
    void authenticateUser_shouldReturnTokens_whenInputValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.findByEmail(any(EmailAddress.class))).thenReturn(Optional.of(userEntity));
        when(jwtService.generateAccessToken(ArgumentMatchers.any(), any(UserDetails.class))).thenReturn(mockToken);
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("54321");

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.authenticate(userEntity.getEmail(), userEntity.getPasswordObject());

        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getAccessToken()).isEqualTo(mockToken);
        assertThat(result.asOption1().getRefreshToken()).isEqualTo("54321");

    }

    @Test
    void authenticateUser_shouldReturnFail_whenEmailNotExists() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenAnswer(i -> i.getArguments()[0]);

        when(userRepository.findByEmail(any(EmailAddress.class))).thenReturn(Optional.empty());

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.authenticate(userEntity.getEmail(), userEntity.getPasswordObject());

        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void refreshToken_ShouldReturnTokens_whenInputValid() throws IOException {
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());
        when(userRepository.findByEmail(any(EmailAddress.class))).thenReturn(Optional.of(userEntity));
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.generateAccessToken(any(UserDetails.class))).thenReturn("678910");

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.refreshToken(httpRequest);

        assertThat(result.hasOption1()).isTrue();
        assertThat(result.asOption1().getAccessToken()).isEqualTo("678910");
        assertThat(result.asOption1().getRefreshToken()).isEqualTo(mockToken);
    }

    @Test
    void refreshToken_ShouldReturnUnAuthorized_whenAuthHeaderMissing() throws IOException {
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn("");

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.refreshToken(httpRequest);

        assertThat(result.hasOption2()).isTrue();
        assertThat(result.asOption2().getErrorType()).isEqualTo(ValidationErrorType.Unauthorized);
    }

    @Test
    void refreshToken_ShouldReturnUnAuthorized_whenEmailMissing() throws IOException {
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.extractUserName(any(String.class))).thenReturn("");

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.refreshToken(httpRequest);

        assertThat(result.hasOption2()).isTrue();
        assertThat(result.asOption2().getErrorType()).isEqualTo(ValidationErrorType.Unauthorized);
    }

    @Test
    void refreshToken_ShouldThrow_whenUserEmailNotExists() {
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());
        when(userRepository.findByEmail(any(EmailAddress.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.refreshToken(httpRequest))
                .isInstanceOf(BadCredentialsException.class);

    }

    @Test
    void refreshToken_ShouldReturnUnauthorized_whenRefreshTokenIsExpired() throws IOException {
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());
        when(userRepository.findByEmail(any(EmailAddress.class))).thenReturn(Optional.of(userEntity));
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(true);

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.refreshToken(httpRequest);

        assertThat(result.hasOption2()).isTrue();
        assertThat(result.asOption2().getErrorType()).isEqualTo(ValidationErrorType.Unauthorized);
    }


    @Test
    void updateUserName_ShouldReturnSuccess_whenInputValid() {

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        //existing email/username
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        //new email/username
        when(userRepository.findByEmail(updatedEmail)).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).then(returnsFirstArg());

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.updateUserName(httpRequest, updatedEmail);


        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    void updateUserName_ShouldFail_whenTokenMissing() {

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn("");

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.updateUserName(httpRequest, updatedEmail);

        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void updateUserName_ShouldFail_whenRequestEmailNotExists() {

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        //existing email/username
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateUserName(httpRequest, updatedEmail))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    void updateUserName_ShouldFail_whenNewEmailEmpty() {
        final EmailAddress newUserName = EmailAddress.empty();

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        //existing email/username
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.updateUserName(httpRequest, newUserName);

        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    void updateUserName_ShouldFail_whenNewEmailExists() {

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        //existing email/username
        //existing email/username
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        //new email/username
        when(userRepository.findByEmail(updatedEmail)).thenReturn(Optional.of(userEntity));

        OneOf2<AuthenticationResult, ValidationFailed> result = underTest.updateUserName(httpRequest, updatedEmail);

        assertThat(result.hasOption2()).isTrue();
    }

    @Test
    void updatePassword_ShouldReturnSuccess_whenInputValid() {

        userEntity.setId(100L);
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        //existing email/username
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).then(returnsFirstArg());

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword), updatedPassword);

        assertThat(result.hasOption1()).isTrue();

    }

    @Test
    void updatePassword_ShouldFail_whenOldPasswordWrong() {

        userEntity.setId(100L);
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn(userEntity.getUsername());

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).then(returnsFirstArg());

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword + "xyz"), updatedPassword);


        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void updatePassword_ShouldFail_whenAuthTokenMissing() {

        userEntity.setId(100L);
        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn("");

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword), updatedPassword);


        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void updatePassword_ShouldFail_whenAuthTokenExpired() {

        userEntity.setId(100L);

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(true);

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword), updatedPassword);

        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void updatePassword_ShouldFail_whenEmailMissing() {

        userEntity.setId(100L);

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn("");

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword), updatedPassword);

        assertThat(result.hasOption2()).isTrue();

    }

    @Test
    void updatePassword_ShouldFail_whenUserNameNotExists() {

        userEntity.setId(100L);

        when(authHeaderParser.getAuthToken(any(HttpServletRequest.class))).thenReturn(mockToken);
        when(jwtService.isTokenExpired(any(String.class))).thenReturn(false);
        when(jwtService.extractUserName(any(String.class))).thenReturn("");

        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        OneOf2<Success, ValidationFailed> result = underTest.updatePassword(httpRequest,
                Password.from(userPassword), updatedPassword);

        assertThat(result.hasOption2()).isTrue();

    }
}