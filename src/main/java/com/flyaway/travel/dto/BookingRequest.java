package com.flyaway.travel.dto;

import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "flightId is required")
        Long flightId
) {}