package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.repository.EmployeeRepository;
import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.RegistrationResponse;
import com.spring.jwt.SpringAppWithJwt.responseObjects.JwtResponse;
import com.spring.jwt.SpringAppWithJwt.utils.JwtTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;

    public RegistrationResponse registration(String username,String password){
        Employee e = new Employee();
        e.setUsername(username);
        e.setPassword(passwordEncoder.encode(password));
        repository.save(e);
        return RegistrationResponse.builder().welcome("User " + username + " was successfully registered").build();

    }

    public JwtResponse authentication(String username,String password) throws UsernameNotFoundException{
        var user = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username,password));
        Employee employee = repository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found "));
        var jwtAccessToken = jwtTokenService.generateAccessToken(employee);
        var jwtRefreshToken = jwtTokenService.generateRefreshToken(employee);
        employee.setJwtRefreshToken(jwtRefreshToken);
        repository.save(employee);
        return JwtResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken).build();

    }
    public ResponseEntity<JwtResponse> generateAccessToken(@NonNull String refreshToken){
        if(!jwtTokenService.validateRefreshToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var claims = jwtTokenService.getRefreshClaims(refreshToken);
        var username = claims.getSubject();

        var user = userDetailsService.loadUserByUsername(username);

        var jwtAcccessToken = jwtTokenService.generateAccessToken(user);
        return ResponseEntity.ok(JwtResponse.builder().accessToken(jwtAcccessToken).build());
    }
    public ResponseEntity<JwtResponse> generateRefreshToken(@NonNull String refreshToken){
        if(!jwtTokenService.validateRefreshToken(refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var claims = jwtTokenService.getRefreshClaims(refreshToken);
        var username = claims.getSubject();

        var user = userDetailsService.loadUserByUsername(username);

        var jwtAcccessToken = jwtTokenService.generateAccessToken(user);
        var jwtRefreshToken = jwtTokenService.generateRefreshToken(user);
        return ResponseEntity.ok(JwtResponse.builder().accessToken(jwtAcccessToken).refreshToken(refreshToken).build());
    }
}
