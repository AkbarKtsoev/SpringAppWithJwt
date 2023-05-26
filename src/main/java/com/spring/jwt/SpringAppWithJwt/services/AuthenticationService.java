package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.repository.UserRepository;
import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationResponse;
import com.spring.jwt.SpringAppWithJwt.responseObjects.RegistrationRequest;
import com.spring.jwt.SpringAppWithJwt.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthenticationResponse registration(RegistrationRequest regreq){
        System.out.println(regreq);

        Employee e = new Employee();
        e.setUsername(regreq.getUsername());
        e.setPassword(passwordEncoder.encode(regreq.getPassword()));
        e.setRole(regreq.getRole());
        repository.save(e);
        var jwtToken = jwtTokenService.generateToken(e);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    public AuthenticationResponse authentication(AuthenticationRequest authreq){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authreq.getUsername(),authreq.getPassword()));

        var user = repository.findByUsername(authreq.getUsername()).orElseThrow(null);
        var jwtToken = jwtTokenService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
