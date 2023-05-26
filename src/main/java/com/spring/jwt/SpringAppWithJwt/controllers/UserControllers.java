package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.models.Consumer;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingResponse;
import com.spring.jwt.SpringAppWithJwt.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllers {
    private final BookingService bookingService;
    @RequestMapping("/consumers")
    public List<Consumer> seeAllConsumers(){
        return bookingService.seeAllConsumers();
    }
    @RequestMapping("/buyTickets")
    public ResponseEntity<BuyingResponse> buyTicket(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(bookingService.buyTicket(buyingRequest.getConsumerid(),buyingRequest.getEventid(),
                buyingRequest.getAmountoftickets()));

    }
    @RequestMapping("/returnTickets")
    public ResponseEntity<BuyingResponse> returnTicket(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(bookingService.returnTickets(buyingRequest));

    }
}
