package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.AuthenticationResponse;
import com.spring.jwt.SpringAppWithJwt.responseObjects.RegistrationRequest;
import com.spring.jwt.SpringAppWithJwt.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ar")
@RequiredArgsConstructor
public class JwtAuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> registerUserAndCreateToken(@RequestBody RegistrationRequest regreq){

        return ResponseEntity.ok(authenticationService.registration(regreq));

    }
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticationUser(@RequestBody AuthenticationRequest authreq){
        return ResponseEntity.ok(authenticationService.authentication(authreq));
    }



}
