package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.repository.UserRepository;
import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationResponse;
import com.spring.jwt.SpringAppWithJwt.responseObjects.RegistrationRequest;
import com.spring.jwt.SpringAppWithJwt.utils.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthenticationResponse registration(RegistrationRequest regreq){
        var user = User.builder().username(regreq.getUsername())
                .password(passwordEncoder.encode(regreq.getPassword())).build();
//        userRepository.save(user);
        var jwtToken = jwtTokenService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
