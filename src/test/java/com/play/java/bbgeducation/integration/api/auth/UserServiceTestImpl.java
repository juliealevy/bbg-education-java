package com.play.java.bbgeducation.integration.api.auth;

import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserServiceTestImpl implements UserServiceTest{
    private final UserRepository userRepository;

    public UserServiceTestImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
}
