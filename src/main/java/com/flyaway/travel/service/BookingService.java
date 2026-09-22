package com.flyaway.travel.service;

import com.flyaway.travel.dto.BookingRequest;
import com.flyaway.travel.dto.BookingResponse;
import com.flyaway.travel.exception.BadRequestException;
import com.flyaway.travel.exception.ResourceNotFoundException;
import com.flyaway.travel.model.Booking;
import com.flyaway.travel.model.Flight;
import com.flyaway.travel.model.User;
import com.flyaway.travel.repository.BookingRepository;
import com.flyaway.travel.repository.FlightRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final EmailService emailService;

    public BookingService(BookingRepository bookingRepository,
                          FlightRepository flightRepository,
                          EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
        this.emailService = emailService;
    }

    @Transactional
    public BookingResponse book(BookingRequest request, User customer) {
        Flight flight = flightRepository.findById(request.flightId())
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));

        if (flight.getAvailableSeats() <= 0) {
            throw new BadRequestException("No seats available");
        }

        if (flight.getEstDepartureTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book a flight in the past or in transit");
        }

        // Conflicto de horario: buscar reservas activas del usuario que se solapen
        List<Booking> existing = bookingRepository.findByCustomer(customer);
        for (Booking b : existing) {
            Flight f = b.getFlight();
            boolean overlap = flight.getEstDepartureTime().isBefore(f.getEstArrivalTime())
                    && f.getEstDepartureTime().isBefore(flight.getEstArrivalTime());
            if (overlap) {
                throw new BadRequestException("Booking conflict: user has another flight in this time range");
            }
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        Booking booking = Booking.builder()
                .flight(flight)
                .customer(customer)
                .bookingDate(LocalDateTime.now())
                .build();

        Booking saved = bookingRepository.save(booking);

        // Nice to have: generar archivo de confirmacion
        try {
            emailService.generateConfirmationFile(saved);
        } catch (Exception e) {
            // No interrumpir la reserva si falla el email
        }

        return BookingResponse.from(saved);
    }

    public BookingResponse findById(Long id, User customer) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (!booking.getCustomer().getId().equals(customer.getId())) {
            throw new ResourceNotFoundException("Booking not found");
        }
        return BookingResponse.from(booking);
    }
}