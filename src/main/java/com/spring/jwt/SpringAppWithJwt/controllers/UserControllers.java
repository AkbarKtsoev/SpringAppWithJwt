package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.BuyingResponse;
import com.spring.jwt.SpringAppWithJwt.services.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserControllers {
    private final AuthenticationManager authenticationManager;
    private final BookingService bookingService;
    @RequestMapping("/consumers")
    public List<Employee> seeAllConsumers(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
//        System.out.println((UserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        var user = authentication.getPrincipal();
        System.out.println(authentication.getName());
        return bookingService.seeAllConsumers();

    }
    @PostAuthorize("#buyingRequest.name == authentication.name")
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
