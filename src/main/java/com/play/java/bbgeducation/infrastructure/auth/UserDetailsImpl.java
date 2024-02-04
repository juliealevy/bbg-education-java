package com.play.java.bbgeducation.infrastructure.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetailsImpl extends User {
    private static final long serialVersionUID = -3531439484732724601L;
    private final String firstName, lastName;


    public UserDetailsImpl(
            String username, String password,
            boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities,
            String firstName, String lastName) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static long getSerialVersionid(){
        return serialVersionUID;
    }

    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
}
