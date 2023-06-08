package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.exceptionObjects.EventNotFoundException;
import com.spring.jwt.SpringAppWithJwt.models.Event;
import com.spring.jwt.SpringAppWithJwt.models.Role;
import com.spring.jwt.SpringAppWithJwt.responseObjects.*;
import com.spring.jwt.SpringAppWithJwt.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResourceController {
    private final BookingService bookingService;
    @RequestMapping("/hellouser")
    public String helloUser(){
        return "Hello User";
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @RequestMapping("/helloadmin")
    public String helloAdmin(){
        return "Hello admin";
    }
    @RequestMapping("/events")
    public List<EventResponse>events(){
        return bookingService.showAllEvents();


    }
    @RequestMapping("/events/{id}")
    public ResponseEntity<EventResponse> showEventById(@PathVariable("id") int id){
        return ResponseEntity.ok(bookingService.showEventById(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/events/createEvent")
    public ResponseEntity<CreationResponce> createEvent(@RequestBody EventRequest eventRequest) throws Exception {
        return ResponseEntity.ok(bookingService.createEvent(eventRequest.getName(),eventRequest.getDate(), eventRequest.getAmountOfTickets(), eventRequest.getTicketPrice()));

    }
    @PostMapping("/events/free")
    public String freeTickets(){
        return String.valueOf(bookingService.freeTickets());
    }

    @ExceptionHandler
    public ResponseEntity<UserNotFoundErrorResponse> checkException(UsernameNotFoundException u){
        UserNotFoundErrorResponse us = new UserNotFoundErrorResponse(u.getMessage());
        return new ResponseEntity<>(us,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler
    public ResponseEntity<EventNotFoundErrorResponse> checkException(EventNotFoundException u){
        EventNotFoundErrorResponse us = new EventNotFoundErrorResponse(u.getMessage());
        return new ResponseEntity<>(us,HttpStatus.NOT_FOUND);

    }

}
