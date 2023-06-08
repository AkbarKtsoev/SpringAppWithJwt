package com.spring.jwt.SpringAppWithJwt.repository;

import com.spring.jwt.SpringAppWithJwt.models.Event;
import com.spring.jwt.SpringAppWithJwt.models.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedNativeQueries;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Integer> {
    @Query(value = "select count(t) from Ticket t where t.owner is null")
    int findTicketWithoutOwner();
    @Query("select count(t) from Ticket t where t.event = :id")
    List<Ticket> findByEvent_Id(@Param("id") Event e);
    @Query("select t from Ticket t where t.event = :id and t.owner is null ")
    int findByEventAndOwner(@Param("id") Event e);


    @Query("select t from Ticket t where t.event = :id and t.owner is not null")
    List<Ticket> selectionWhereOwnerIsNotNull(@Param("id") Event e);

}
