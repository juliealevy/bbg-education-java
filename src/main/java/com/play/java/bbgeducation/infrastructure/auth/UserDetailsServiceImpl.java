package com.play.java.bbgeducation.infrastructure.auth;

import com.play.java.bbgeducation.domain.users.UserEntity;
import com.play.java.bbgeducation.infrastructure.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

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

        UserEntity userEntity = user.get();

        return new UserDetailsImpl(
                userEntity.getEmail(),
                userEntity.getPassword(),
                true,true,true,true,
                buildAuthorities(),userEntity.getFirstName(),userEntity.getLastName());
    }

    private String[] getUserRoles(){
        //TODO:  create a provider and fetch this info when more roles needed
        //or add to user entity and fetch with that call
        List<String> roles = new ArrayList<>();
        //all users get this role if they authenticate
        roles.add("USER");
        return roles.toArray(new String[0]);
    }

    private Collection<GrantedAuthority> buildAuthorities(){
        String[] roles = getUserRoles();
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for(String role : roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities;
    }


}
