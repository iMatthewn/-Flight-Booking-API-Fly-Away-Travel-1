package com.flyaway.travel.controller;

import com.flyaway.travel.dto.BookingRequest;
import com.flyaway.travel.dto.BookingResponse;
import com.flyaway.travel.dto.FlightRequest;
import com.flyaway.travel.dto.FlightResponse;
import com.flyaway.travel.model.User;
import com.flyaway.travel.repository.UserRepository;
import com.flyaway.travel.exception.BadRequestException;
import com.flyaway.travel.service.BookingService;
import com.flyaway.travel.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private final FlightService flightService;
    private final BookingService bookingService;
    private final UserRepository userRepository;

    public FlightController(FlightService flightService,
                            BookingService bookingService,
                            UserRepository userRepository) {
        this.flightService = flightService;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<FlightResponse> create(@Valid @RequestBody FlightRequest request) {
        FlightResponse response = flightService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponse>> search(
            @RequestParam(required = false) String flightNumber,
            @RequestParam(required = false) String airlineName,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return ResponseEntity.ok(flightService.search(flightNumber, airlineName, startDate, endDate));
    }

    @PostMapping("/book")
    public ResponseEntity<BookingResponse> book(@Valid @RequestBody BookingRequest request,
                                                Authentication authentication) {
        User customer = currentUser(authentication);
        BookingResponse response = bookingService.book(request, customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable Long id,
                                                      Authentication authentication) {
        User customer = currentUser(authentication);
        return ResponseEntity.ok(bookingService.findById(id, customer));
    }

    private User currentUser(Authentication authentication) {
        if (authentication == null) {
            throw new BadRequestException("Not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));
    }
}