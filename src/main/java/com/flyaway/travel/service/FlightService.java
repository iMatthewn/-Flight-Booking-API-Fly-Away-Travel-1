package com.flyaway.travel.service;

import com.flyaway.travel.dto.FlightRequest;
import com.flyaway.travel.dto.FlightResponse;
import com.flyaway.travel.exception.BadRequestException;
import com.flyaway.travel.exception.ResourceNotFoundException;
import com.flyaway.travel.model.Flight;
import com.flyaway.travel.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public FlightResponse create(FlightRequest request) {
        if (flightRepository.existsByFlightNumber(request.flightNumber())) {
            throw new BadRequestException("Flight already exists");
        }
        if (!request.estDepartureTime().isBefore(request.estArrivalTime())) {
            throw new BadRequestException("estDepartureTime must be before estArrivalTime");
        }

        Flight flight = Flight.builder()
                .airlineName(request.airlineName())
                .flightNumber(request.flightNumber())
                .estDepartureTime(request.estDepartureTime())
                .estArrivalTime(request.estArrivalTime())
                .availableSeats(request.availableSeats())
                .build();

        Flight saved = flightRepository.save(flight);
        return FlightResponse.from(saved);
    }

    public List<FlightResponse> search(String flightNumber, String airlineName,
                                       LocalDateTime startDate, LocalDateTime endDate) {
        List<Flight> flights;

        if (flightNumber != null && !flightNumber.isBlank()) {
            flights = flightRepository.findByFlightNumberContainingIgnoreCase(flightNumber);
        } else if (airlineName != null && !airlineName.isBlank()) {
            flights = flightRepository.findByAirlineNameContainingIgnoreCase(airlineName);
        } else if (startDate != null && endDate != null) {
            flights = flightRepository.findByEstDepartureTimeBetween(startDate, endDate);
        } else {
            flights = flightRepository.findAll();
        }

        return flights.stream().map(FlightResponse::from).toList();
    }

    public Flight findById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found"));
    }
}