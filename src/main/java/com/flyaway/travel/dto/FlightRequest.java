package com.flyaway.travel.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record FlightRequest(
        @NotBlank(message = "airlineName is required")
        String airlineName,

        @NotBlank(message = "flightNumber is required")
        @Pattern(regexp = "^[A-Z]{2,3}[0-9]{3}$",
                 message = "flightNumber debe coincidir con ^[A-Z]{2,3}[0-9]{3}$ (ej: AA123)")
        String flightNumber,

        @NotNull(message = "estDepartureTime is required")
        LocalDateTime estDepartureTime,

        @NotNull(message = "estArrivalTime is required")
        LocalDateTime estArrivalTime,

        @NotNull(message = "availableSeats is required")
        @Min(value = 1, message = "availableSeats debe ser mayor que o igual a 1")
        Integer availableSeats
) {}