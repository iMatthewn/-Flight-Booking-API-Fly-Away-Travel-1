package com.flyaway.travel.dto;

import com.flyaway.travel.model.Booking;
import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long flightId,
        String flightNumber,
        String airlineName,
        LocalDateTime estDepartureTime,
        LocalDateTime estArrivalTime,
        LocalDateTime bookingDate,
        Long customerId,
        String customerFullName
) {
    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getFlight().getId(),
                b.getFlight().getFlightNumber(),
                b.getFlight().getAirlineName(),
                b.getFlight().getEstDepartureTime(),
                b.getFlight().getEstArrivalTime(),
                b.getBookingDate(),
                b.getCustomer().getId(),
                b.getCustomer().getFirstName() + " " + b.getCustomer().getLastName()
        );
    }
}