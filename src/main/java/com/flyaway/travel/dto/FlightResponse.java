package com.flyaway.travel.dto;

import com.flyaway.travel.model.Flight;
import java.time.LocalDateTime;

public record FlightResponse(
        Long id,
        String airlineName,
        String flightNumber,
        LocalDateTime estDepartureTime,
        LocalDateTime estArrivalTime,
        Integer availableSeats
) {
    public static FlightResponse from(Flight f) {
        return new FlightResponse(
                f.getId(),
                f.getAirlineName(),
                f.getFlightNumber(),
                f.getEstDepartureTime(),
                f.getEstArrivalTime(),
                f.getAvailableSeats()
        );
    }
}