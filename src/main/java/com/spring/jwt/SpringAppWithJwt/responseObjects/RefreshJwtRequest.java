package com.spring.jwt.SpringAppWithJwt.responseObjects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RefreshJwtRequest {
    String refreshToken;
}
