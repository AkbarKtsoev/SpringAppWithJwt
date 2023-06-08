package com.spring.jwt.SpringAppWithJwt.utils;

import com.spring.jwt.SpringAppWithJwt.repository.EmployeeRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService {

    private final EmployeeRepository employeeRepository;

    private SecretKey jwtAccessSecret;
    private SecretKey jwtRefreshSecret;

    public JwtTokenService(EmployeeRepository employeeRepository, @Value("${jwt.secret.access}") String jwtAccessSecret,
                           @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
        this.employeeRepository = employeeRepository;
        this.jwtAccessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.jwtRefreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret));
    }

    public String generateAccessToken(@NonNull UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .claim("roles",userDetails.getAuthorities())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60*5))
                .signWith(jwtAccessSecret).compact();
    }
    public String generateRefreshToken(@NonNull UserDetails userDetails){
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis()+ 100000 * 60 * 60))
                .signWith(jwtRefreshSecret)
                .compact();
    }
    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }
    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
    //Кусок кода позаимствовал с сайта https://struchkov.dev/blog/ru/jwt-implementation-in-spring/
        // в целом пока копался понял что моя реализация крайне простенькая и чтоб не копаться в дебрях
        // взял кусок более адекватно собранного колеса
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
//            log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
//            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
//            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
//            log.error("Invalid signature", sEx);
        } catch (Exception e) {
//            log.error("invalid token", e);
        }
        return false;
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public Claims getAccessClaims(@NonNull String token) {
    return getClaims(token, jwtAccessSecret);
}

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtRefreshSecret);
    }




}
