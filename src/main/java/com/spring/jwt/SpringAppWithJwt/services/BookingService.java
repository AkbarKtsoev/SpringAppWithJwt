package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.models.Consumer;
import com.spring.jwt.SpringAppWithJwt.models.Event;
import com.spring.jwt.SpringAppWithJwt.models.Ticket;
import com.spring.jwt.SpringAppWithJwt.repository.ConsumerRepository;
import com.spring.jwt.SpringAppWithJwt.repository.EventRepository;
import com.spring.jwt.SpringAppWithJwt.repository.TicketRepository;
import com.spring.jwt.SpringAppWithJwt.responseObjects.*;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.OSUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final ConsumerRepository consumerRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    int year = LocalDateTime.now().getYear();
    Month month = LocalDateTime.now().getMonth();

    public List<EventResponse> showAllEvents(){
        List<Event> e = eventRepository.findAll();
        List<EventResponse> listit = new ArrayList<>();
        for (Event eve:e){
            listit.add(EventResponse.builder().eventName(eve.getEventName()).localDateTime(eve.getLocalDateTime())
                    .freeTickets(ticketRepository.findByEvent_Id(eve)).build());
        }
        return listit;
    }
    public EventResponse showEventById(int id){
        Event e = eventRepository.findById(id).orElse(null);
//        System.out.println(ticketRepository.findByEvent_Id(id));
//        e.setTickets(ticketRepository.findByEvent_Id(id));

        return EventResponse.builder().eventName(e.getEventName()).localDateTime(e.getLocalDateTime())
                .freeTickets(ticketRepository.findByEvent_Id(e)).build();
    }
    public CreationResponce createEvent(EventRequest eventRequest){

        String name = eventRequest.getName();
        int date = eventRequest.getDate();
        int amountOfTickets = eventRequest.getAmountOfTickets();

        Event e = new Event();
        LocalDateTime localDateTime = LocalDateTime.of(year,month,date,00,00,00);
        e.setEventName(name);
        e.setLocalDateTime(localDateTime);
        e.setTickets(ticketRepository.findAll());
        eventRepository.save(e);

        for (int i = 0;i<amountOfTickets;i++){
            Ticket t = new Ticket();
            t.setEvent(e);
            t.setPrice(eventRequest.getTicketPrice());
            ticketRepository.save(t);
        }
        return CreationResponce.builder().amountOfTickets(amountOfTickets).price(eventRequest.getTicketPrice()).event("U created event " + e.getEventName()).build();
    }
    public int freeTickets(){
       return ticketRepository.findTicketWithoutOwner();
    }
    public List<Consumer> seeAllConsumers(){
        return consumerRepository.findAll();
    }
    public BuyingResponse buyTicket(int consumer_id, int event_id, int amountOfTickets){
        Consumer consumer = consumerRepository.findById(consumer_id).orElse(null);
        Event event = eventRepository.findById(event_id).orElse(null);
        List<Ticket> list = ticketRepository.findByEventAndOwner(event);
        double priceForOneTicket = list.get(0).getPrice();
        double consumerBalance = consumer.getBalance();
        for (int i = 0;i<amountOfTickets;i++){
            System.out.println("ok");
            if (consumerBalance>priceForOneTicket){
                consumer.setAmountOfTickets(+i);
                consumer.setBalance(consumerBalance-priceForOneTicket);
                list.get(i).setOwner(consumer);
                consumerRepository.save(consumer);
                ticketRepository.save(list.get(i));
            }else {
                break;
            }
        }
        return BuyingResponse.builder().response("U bought ticket on " + event.getEventName() + " in amount " + amountOfTickets).build();
    }

    public BuyingResponse returnTickets(BuyingRequest buyingRequest){
        Consumer consumer = consumerRepository.findById(buyingRequest.getConsumerid()).orElse(null);
        Event event = eventRepository.findById(buyingRequest.getEventid()).orElse(null);
        List<Ticket> list = ticketRepository.selectionWhereOwnerIsNotNull(event);
        double priceForOneTicket = list.get(0).getPrice();
        double consumerBalance = consumer.getBalance();
        int amountOfTickets = buyingRequest.getAmountoftickets();
        for (int i = 0;i<amountOfTickets;i++){
            if (consumerBalance>priceForOneTicket){
                consumer.setAmountOfTickets(-i);
                consumer.setBalance(i*list.get(i).getPrice());
                list.get(i).setOwner(null);
                consumerRepository.save(consumer);
                ticketRepository.save(list.get(i));
            }else {
                break;
            }
        }
        return BuyingResponse.builder().response("U returned all tickets in amount " + amountOfTickets).build();
    }
}
