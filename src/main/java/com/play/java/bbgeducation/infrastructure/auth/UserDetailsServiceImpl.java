package com.play.java.bbgeducation.infrastructure.auth;

import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException("Email is not registered.");

        return User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .roles(getUserRoles())
                .build();
    }

    private String[] getUserRoles(){
        //TODO:  create a provider and fetch this info when more roles needed
        List<String> roles = new ArrayList<>();
        //all users get this role if they authenticate
        roles.add("USER");
        return roles.toArray(new String[0]);
    }
}
