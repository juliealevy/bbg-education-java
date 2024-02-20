package com.play.java.bbgeducation.infrastructure.auth;

import an.awesome.pipelinr.repack.com.google.common.base.Strings;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class AuthHeaderParser {
    public String getAuthToken(HttpServletRequest request){
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!Strings.isNullOrEmpty(authHeader) && (authHeader.startsWith("Bearer "))){
            return authHeader.substring(7);
        }
        return "";
    }
}
