package com.spring.jwt.SpringAppWithJwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import sun.nio.cs.HKSCS;

import java.security.Key;
import java.util.Date;

@Service

public class JwtTokenService {
    @Value("${jwt.secret}")
    private  String SECRET_KEY;

    public String generateToken(UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 60))
                .signWith(gitSignKey()).compact();
    }
    public Boolean validationToken(String token,UserDetails userDetails){
        final String username = userDetails.getUsername();
        return (username.equals(userDetails.getUsername()) && !claims(token).getExpiration().before(new Date()));
    }

    public Claims claims(String token){
        return Jwts.parserBuilder().setSigningKey(gitSignKey())
                .build().parseClaimsJws(token)
                .getBody();
    }
    public Key gitSignKey(){
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
