package com.flyaway.travel.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights", uniqueConstraints = @UniqueConstraint(columnNames = "flight_number"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airline_name", nullable = false)
    private String airlineName;

    @Column(name = "flight_number", nullable = false, unique = true, length = 6)
    private String flightNumber;

    @Column(name = "est_departure_time", nullable = false)
    private LocalDateTime estDepartureTime;

    @Column(name = "est_arrival_time", nullable = false)
    private LocalDateTime estArrivalTime;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
}