package com.tmnhat.bookingservice.validation;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.exception.BadRequestException;
import java.time.LocalDateTime;

public class BookingValidator {

    public static void validateBooking(Booking booking) {
        if (booking == null) {
            throw new BadRequestException("Booking data is required");
        }

        if (booking.getBookingName() == null || booking.getBookingName().trim().isEmpty()) {
            throw new BadRequestException("Booking name cannot be empty");
        }

        if (booking.getEventId() == null || booking.getEventId() <= 0) {
            throw new BadRequestException("Event ID must be greater than 0");
        }

        if (booking.getUserId() == null || booking.getUserId() <= 0) {
            throw new BadRequestException("User ID must be greater than 0");
        }

        if (booking.getTicketCount() <= 0) {
            throw new BadRequestException("Ticket count must be greater than 0");
        }

        if (booking.getStatus() == null || booking.getStatus().trim().isEmpty()) {
            throw new BadRequestException("Booking status cannot be empty");
        }

        if (booking.getCreatedAt() == null || booking.getCreatedAt().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("CreatedAt must be a valid date/time in the past");
        }
    }

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid booking ID");
        }
    }
}
