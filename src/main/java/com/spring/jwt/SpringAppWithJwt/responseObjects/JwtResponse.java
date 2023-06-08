package com.spring.jwt.SpringAppWithJwt.responseObjects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {
    private final String type = "Bearer";

    private String accessToken;

    private String refreshToken;

}

