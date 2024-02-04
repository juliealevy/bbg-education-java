package com.play.java.bbgeducation.infrastructure.auth;

import an.awesome.pipelinr.repack.com.google.common.base.Strings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.play.java.bbgeducation.application.common.oneof.OneOf2;
import com.play.java.bbgeducation.application.common.oneof.oneoftypes.NotFound;
import com.play.java.bbgeducation.application.users.UserResult;
import com.play.java.bbgeducation.application.users.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;
    private final ObjectMapper mapper;

    private final UserService userService;

    public JwtAuthorizationFilter(JwtTokenUtil jwtUtil, ObjectMapper mapper, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Map<String, Object> errorDetails = new HashMap<>();
        try{
            String accessToken = jwtUtil.resolveToken(request);

            if (Strings.isNullOrEmpty(accessToken)){
                filterChain.doFilter(request, response);
                return;
            }

            Claims claims = jwtUtil.resolveClaims(request);
            if (claims != null && jwtUtil.validateClaims(claims)) {
                String email = jwtUtil.getEmail(claims);
                //get user info without password
                OneOf2<UserResult, NotFound> userEntity = userService.getByEmail(email);
                if (userEntity.hasOption2()) {
                    throw new BadCredentialsException("Invalid Credentials");
                }
                UserResult user = userEntity.asOption1();
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, "", buildAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(Exception ex){
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(response.getWriter(), errorDetails);
        }
        filterChain.doFilter(request,response);

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
