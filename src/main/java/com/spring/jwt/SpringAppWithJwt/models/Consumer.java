package com.spring.jwt.SpringAppWithJwt.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "consumer")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "amountoftickets")
    private Integer amountOfTickets;
    @Column(name = "balance")
    private Double balance;
    @JsonIgnore
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    private List<Ticket> listOfTickets;
}
