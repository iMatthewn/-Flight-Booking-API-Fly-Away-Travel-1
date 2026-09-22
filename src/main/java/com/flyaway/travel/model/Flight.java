package com.flyaway.travel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "airlineName is required")
    @Column(name = "airline_name", nullable = false)
    private String airlineName;

    @NotBlank(message = "flightNumber is required")
    @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$",
             message = "flightNumber debe coincidir con ^[A-Z]{2,3}[0-9]{3}$ (ej: AA123)")
    @Column(name = "flight_number", nullable = false, unique = true, length = 6)
    private String flightNumber;

    @NotNull(message = "estDepartureTime is required")
    @Column(name = "est_departure_time", nullable = false)
    private LocalDateTime estDepartureTime;

    @NotNull(message = "estArrivalTime is required")
    @Column(name = "est_arrival_time", nullable = false)
    private LocalDateTime estArrivalTime;

    @NotNull(message = "availableSeats is required")
    @Min(value = 1, message = "availableSeats debe ser mayor que o igual a 1")
    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;
}
