package com.play.java.bbgeducation.infrastructure.auth;

import com.play.java.bbgeducation.domain.users.UserEntity;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenUtil {

   // @Value("${jwt.secret}")  issue creating bean with these.. TODO:  figure out
    private String secret = "testing123notrealsecret";

    //@Value("${jwt.access-token-lifetime-seconds}")
    private long tokenLifeTimeSeconds = 36000;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";
    private final String ATTRIBUTE_EXPIRED = "expired";
    private final String ATTRIBUTE_INVALID = "invalid";

    public String generateToken(UserEntity user){
        Claims claims = buildClaims(user);
        Date tokenExpiration = getTokenExpiration();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setClaims(claims)
                .setExpiration(tokenExpiration)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Claims resolveClaims(HttpServletRequest req){
        try{
            String token = resolveToken(req);
            if (token != null){
                return parseJwtClaims(token);
            }
            return null;
        }catch(ExpiredJwtException jex){
            req.setAttribute(ATTRIBUTE_EXPIRED, jex.getMessage());
            throw jex;
        }catch(Exception ex){
            req.setAttribute(ATTRIBUTE_INVALID, ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try{
            return claims.getExpiration().after(new Date());
        }catch(Exception ex){
            throw ex;
        }
    }

    public String getEmail(Claims claims){
        return claims.getSubject();
    }

    private Claims parseJwtClaims(String token){

        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    private Claims buildClaims(UserEntity user){
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("issuer","bbgEducation");
        claims.put("audience","bbgEducation");
        return claims;
    }

    private Date getTokenExpiration(){
        Date tokenCreateDateTime = new Date();
        return new Date(tokenCreateDateTime.getTime() +
                TimeUnit.SECONDS.toMillis(tokenLifeTimeSeconds));
    }
}
