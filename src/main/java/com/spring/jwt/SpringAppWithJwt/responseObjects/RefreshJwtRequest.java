package com.spring.jwt.SpringAppWithJwt.responseObjects;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtRequest {
    String refreshToken;
}
