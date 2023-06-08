package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingResponse;
import com.spring.jwt.SpringAppWithJwt.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllers {
    private final BookingService bookingService;
    @RequestMapping("/consumers")
    public List<Employee> seeAllConsumers(){
        return bookingService.seeAllConsumers();
    }
    @PreAuthorize("#buyingRequest.name == authentication.principal.username")
    @RequestMapping("/buyTickets")
    public ResponseEntity<BuyingResponse> buyTicket(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(bookingService.buyTicket(buyingRequest.getName(),buyingRequest.getEventid(),
                buyingRequest.getAmountoftickets()));

    }
    @RequestMapping("/returnTickets")
    public ResponseEntity<BuyingResponse> returnTicket(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(bookingService.returnTickets(buyingRequest.getName(),buyingRequest.getEventid(),
                buyingRequest.getAmountoftickets()));

    }
}
