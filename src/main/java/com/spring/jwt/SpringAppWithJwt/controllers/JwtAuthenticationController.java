package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.exceptionObjects.UserNotFoundException;
import com.spring.jwt.SpringAppWithJwt.responseObjects.*;
import com.spring.jwt.SpringAppWithJwt.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ar")
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registerUserAndCreateToken(@RequestBody RegistrationRequest regreq){

        return ResponseEntity.ok(authenticationService.registration(regreq.getUsername(),regreq.getPassword()));

    }
    @PostMapping("/authentication")
    public ResponseEntity<JwtResponse> authenticationUser(@RequestBody AuthenticationRequest authreq) throws UsernameNotFoundException{
        return ResponseEntity.ok(authenticationService.authentication(authreq.getUsername(),authreq.getPassword()));
    }
    @PostMapping("/refreshAccessToken")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest){
        return authenticationService.generateAccessToken(refreshJwtRequest.getRefreshToken());

    }
    @ExceptionHandler
    public ResponseEntity<UserNotFoundErrorResponse> checkException(UserNotFoundException u){
        UserNotFoundErrorResponse us = new UserNotFoundErrorResponse(u.getMessage());
        return new ResponseEntity<>(us, HttpStatus.NOT_FOUND);

    }

}
