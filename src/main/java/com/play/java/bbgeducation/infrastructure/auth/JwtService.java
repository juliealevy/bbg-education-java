package com.play.java.bbgeducation.infrastructure.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
public class JwtService {



    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-lifetime-seconds}")
    private int ACCESS_EXPIRE_IN_SECONDS;

    @Value("${jwt.refresh-token-lifetime-seconds}")
    private int REFRESH_EXPIRE_IN_SECONDS;

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public<T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails){
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails) {

        return buildToken(extraClaims, userDetails, getAccessTokenExpiration());
    }

    public String generateRefreshToken(
            UserDetails userDetails) {

        return buildToken(new HashMap<>(), userDetails, getRefreshTokenExpiration());
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              Date expiration){

        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer("bbgeducation")
                .expiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch(ExpiredJwtException jex){
            throw jex;
        }
    }

    private Date getAccessTokenExpiration(){
        Date tokenCreateDateTime = new Date();
        return new Date(tokenCreateDateTime.getTime() +
                TimeUnit.SECONDS.toMillis(ACCESS_EXPIRE_IN_SECONDS));
    }

    private Date getRefreshTokenExpiration(){
        Date tokenCreateDateTime = new Date();
        return new Date(tokenCreateDateTime.getTime() +
                TimeUnit.SECONDS.toMillis(REFRESH_EXPIRE_IN_SECONDS));
    }
}
