package com.spring.jwt.SpringAppWithJwt.services;

import com.spring.jwt.SpringAppWithJwt.exceptionObjects.EventNotFoundException;
import com.spring.jwt.SpringAppWithJwt.models.Employee;
import com.spring.jwt.SpringAppWithJwt.models.Event;
import com.spring.jwt.SpringAppWithJwt.models.Ticket;
import com.spring.jwt.SpringAppWithJwt.repository.EventRepository;
import com.spring.jwt.SpringAppWithJwt.repository.TicketRepository;
import com.spring.jwt.SpringAppWithJwt.repository.EmployeeRepository;
import com.spring.jwt.SpringAppWithJwt.responseObjects.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final EmployeeRepository employeeRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    int year = LocalDateTime.now().getYear();
    Month month = LocalDateTime.now().getMonth();

    public List<EventResponse> showAllEvents(){
        List<EventResponse> listit = new ArrayList<>();
        for (Event eve:eventRepository.findAll()){
            listit.add(EventResponse.builder().eventName(eve.getEventName()).localDateTime(eve.getLocalDateTime())
                    .freeTickets(ticketRepository.findByEventAndOwner(eve))
                    .allTickets(ticketRepository.findByEvent_Id(eve).size()).build());
        }
        return listit;
    }
    public EventResponse showEventById(int id){
        Event e = eventRepository.findById(id).orElseThrow(()->new EventNotFoundException("Such event not found "));

        return EventResponse.builder().eventName(e.getEventName()).localDateTime(e.getLocalDateTime())
                .freeTickets(ticketRepository.findByEventAndOwner(e)).build();
    }
    public CreationResponce createEvent(String name,Integer date,Integer amountofTickets,Double price) throws Exception {
        if (LocalDateTime.now().getDayOfMonth()>date){
            throw new Exception("Date is gone");
        }
        Event e = new Event();
        LocalDateTime localDateTime = LocalDateTime.of(year,month,date,00,00,00);
        e.setEventName(name);
        e.setLocalDateTime(localDateTime);

        for (int i = 0;i<amountofTickets;i++){
            Ticket t = new Ticket();
            t.setEvent(e);
            t.setPrice(price);
            ticketRepository.save(t);
        }
        e.setTickets(ticketRepository.findByEvent_Id(e));
        eventRepository.save(e);
        return CreationResponce.builder().amountOfTickets(amountofTickets).price(price).event("U created event " + name).build();
    }
    public int freeTickets(){
       return ticketRepository.findTicketWithoutOwner();
    }
    public List<Employee> seeAllConsumers(){
        return employeeRepository.findAll();
    }
    public BuyingResponse buyTicket(String name, int event_id, int amountOfTickets){
        Employee employee =  employeeRepository.findByUsername(name)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        Event event = eventRepository.findById(event_id)
                .orElseThrow(()->new EventNotFoundException("Such event was not found "));
         List<Ticket> list =  ticketRepository.findByEvent_Id(event);
         double priceForOneTicket = list.get(0).getPrice();

        for (int i = 0;i<amountOfTickets;i++){

            if (employee.getBalance()>priceForOneTicket){
                employee.setAmountOfTickets(+i);
                employee.setBalance(employee.getBalance()-priceForOneTicket);
                list.get(i).setOwner(employee);
                employeeRepository.save(employee);
                ticketRepository.save(list.get(i));
            }else {
                break;
            }
        }
        return BuyingResponse.builder().response("U bought ticket on " + event.getEventName() + " in amount " + amountOfTickets).build();
    }

    public BuyingResponse returnTickets(String name, int event_id, int amountOfTickets){
        Employee employee =  employeeRepository.findByUsername(name)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        Event event = eventRepository.findById(event_id)
                .orElseThrow(()->new EventNotFoundException("Such event was not found "));
        List<Ticket> list = ticketRepository.selectionWhereOwnerIsNotNull(event);
        double priceForOneTicket = list.get(0).getPrice();
        for (int i = 0;i<amountOfTickets;i++){
            if (employee.getBalance()>priceForOneTicket){
                employee.setAmountOfTickets(-i);
                employee.setBalance(i*list.get(i).getPrice());
                list.get(i).setOwner(null);
                employeeRepository.save(employee);
                ticketRepository.save(list.get(i));
            }else {
                break;
            }
        }
        return BuyingResponse.builder().response("U returned all tickets in amount " + amountOfTickets).build();
    }
}
