package com.spring.jwt.SpringAppWithJwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "eventname")
    private String eventName;
    @Column(name = "timeofevent")
    private LocalDateTime localDateTime;
    @JsonIgnore
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    public List<Ticket> tickets;


}