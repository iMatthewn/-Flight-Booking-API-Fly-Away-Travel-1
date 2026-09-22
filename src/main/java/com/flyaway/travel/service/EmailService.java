package com.flyaway.travel.service;

import com.flyaway.travel.model.Booking;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    public void generateConfirmationFile(Booking booking) throws IOException {
        String fileName = "flight_booking_email_" + booking.getId() + ".txt";
        Path path = Paths.get(fileName);

        String content = """
                Booking Confirmation
                ====================
                Passenger: %s %s
                Flight Number: %s
                Airline: %s
                Estimated Departure: %s
                Estimated Arrival: %s
                Booking Date: %s
                """.formatted(
                booking.getCustomer().getFirstName(),
                booking.getCustomer().getLastName(),
                booking.getFlight().getFlightNumber(),
                booking.getFlight().getAirlineName(),
                booking.getFlight().getEstDepartureTime().format(ISO),
                booking.getFlight().getEstArrivalTime().format(ISO),
                booking.getBookingDate().format(ISO)
        );

        Files.writeString(path, content);
    }
}