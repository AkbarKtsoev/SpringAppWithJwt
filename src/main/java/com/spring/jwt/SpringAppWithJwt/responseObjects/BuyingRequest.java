package com.spring.jwt.SpringAppWithJwt.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyingRequest {
    int consumerid;
    int eventid;
    int amountoftickets;

}
