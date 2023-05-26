package com.spring.jwt.SpringAppWithJwt.controllers;

import com.spring.jwt.SpringAppWithJwt.models.Event;
import com.spring.jwt.SpringAppWithJwt.responseObjects.CreationResponce;
import com.spring.jwt.SpringAppWithJwt.responseObjects.EventRequest;
import com.spring.jwt.SpringAppWithJwt.responseObjects.EventResponse;
import com.spring.jwt.SpringAppWithJwt.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResourceController {
    private final BookingService bookingService;
    @RequestMapping("/hellouser")
    public String helloUser(){
        return "Hello User";
    }

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
    @PostMapping("/events/createEvent")
    public ResponseEntity<CreationResponce> createEvent(@RequestBody EventRequest eventRequest){
        return ResponseEntity.ok(bookingService.createEvent(eventRequest));

    }
    @PostMapping("/events/free")
    public String freeTickets(){
        return String.valueOf(bookingService.freeTickets());
    }



}
