package com.play.java.bbgeducation.infrastructure.auth;

import an.awesome.pipelinr.repack.com.google.common.base.Strings;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthHeaderParser authHeaderParser;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(JwtService jwtService, AuthHeaderParser authHeaderParser, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.authHeaderParser = authHeaderParser;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try{
            final String token = authHeaderParser.getAuthToken(request);
            final String userEmail;

            if (Strings.isNullOrEmpty(token)){
                filterChain.doFilter(request,response);
                return;
            }

            userEmail = jwtService.extractUserName(token);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (!jwtService.isTokenExpired(token)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch(ExpiredJwtException jex){
            writeError(response,jex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch(Exception ex){
            writeError(response, ex.getMessage(), HttpStatus.FORBIDDEN);
        }
        filterChain.doFilter(request,response);

    }

    private void writeError(HttpServletResponse response, String errorMessage, HttpStatus status) throws IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("message", "Authentication Error");
        errorDetails.put("details",errorMessage);
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), errorDetails);
    }
}
