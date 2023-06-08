package com.spring.jwt.SpringAppWithJwt.responseObjects;

import com.spring.jwt.SpringAppWithJwt.models.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    String eventName;
    LocalDateTime localDateTime;
    int freeTickets;
    int allTickets;
}
