package com.play.java.bbgeducation.application.auth;

import com.play.java.bbgeducation.application.common.exceptions.validation.ValidationFailed;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.Success;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.auth.JwtTokenUtil;
import com.play.java.bbgeducation.infrastructure.auth.Roles;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Console;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private JwtTokenUtil jwtUtil;

    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, UserService userService, JwtTokenUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public OneOf2<LoginResponse, ValidationFailed> login(String email, String password) {

        UserDetails userDetails = User.builder()
                .username(email)
                .password(password)
                .roles(Roles.USER)
                .build();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()));

        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            //if this happens, something really screwed up with the authentication
            //add some logging here when logging added
            throw new RuntimeException("An unknown authentication error occurred");
        }
        String token = jwtUtil.generateToken(user.get());
        return OneOf2.fromOption1(LoginResponse.builder()
                .accessToken(token)
                .build());
    }

    @Override
    public OneOf2<Success, ValidationFailed> register(String email, String password, String firstName, String lastName) {

        OneOf2<UserResult, ValidationFailed> user = userService.createUser(firstName, lastName, email, password);
        return user.match(
                success -> OneOf2.fromOption1(new Success()),
                OneOf2::fromOption2
        );
    }
}
