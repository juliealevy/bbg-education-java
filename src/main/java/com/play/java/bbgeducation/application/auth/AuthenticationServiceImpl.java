package com.play.java.bbgeducation.application.auth;

import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.common.validation.EmailExistsValidationFailed;
import com.play.java.bbgeducation.application.common.validation.ValidationFailed;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.auth.JwtService;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OneOf2<Success, ValidationFailed> register(String email, String password, String firstName, String lastName, boolean isAdmin) {

        if (userRepository.existsByEmail(email)){
            return OneOf2.fromOption2(new EmailExistsValidationFailed());
        }

        UserEntity userEntity = UserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .isAdmin(isAdmin)
                .build();

        UserEntity saved = userRepository.save(userEntity);

        //for now, not returning any tokens.  forcing the login call
        return OneOf2.fromOption1(new Success());

    }
    @SneakyThrows
    @Override
    public OneOf2<LoginResult, ValidationFailed> login(String email, String password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        }catch(InternalAuthenticationServiceException ex){
             if (ex.getCause() instanceof BadCredentialsException){
                 throw new BadCredentialsException(ex.getMessage());
             }
        }

        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            //don't want to return info to the user whether email or password was the problem
            return OneOf2.fromOption2(ValidationFailed.Unauthorized("","Bad Credentials"));
        }

        String token = jwtService.generateToken(user.get());

        return OneOf2.fromOption1(LoginResult.builder()
                .accessToken(token)
                .build());
    }


}
