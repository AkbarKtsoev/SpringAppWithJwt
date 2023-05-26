package com.spring.jwt.SpringAppWithJwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;

@Service

public class JwtTokenService {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getSignKey()).compact();
    }
    public Boolean validationToken(String token){
        final String username = getUsernameFromToken(token);
        return (username.equals(claims(token).getSubject()) && !claims(token).getExpiration().before(new Date()));
    }

    public Claims claims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey())
                .build().parseClaimsJws(token)
                .getBody();
    }
    public String getUsernameFromToken(String token){
        return claims(token).getSubject();
    }
    public Key getSignKey(){
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
