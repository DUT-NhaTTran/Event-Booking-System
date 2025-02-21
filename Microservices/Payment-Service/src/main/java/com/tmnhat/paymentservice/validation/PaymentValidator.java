package com.tmnhat.paymentservice.validation;

import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.common.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PaymentValidator {

    private static final List<String> VALID_STATUSES = Arrays.asList("SUCCESS", "FAILED");

    public static void validatePayment(Payment payment) {
        if (payment == null) {
            throw new BadRequestException("Payment data is required");
        }

        if (payment.getBookingId() == null || payment.getBookingId() <= 0) {
            throw new BadRequestException("Booking ID must be greater than 0");
        }

        if (payment.getUserId() == null || payment.getUserId() <= 0) {
            throw new BadRequestException("User ID must be greater than 0");
        }

        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }

        if (payment.getStatus() == null || !VALID_STATUSES.contains(payment.getStatus().toUpperCase())) {
            throw new BadRequestException("Invalid payment status. Allowed values: SUCCESS, FAILED");
        }

        if (payment.getCreatedAt() == null || payment.getCreatedAt().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("CreatedAt must be a valid past or present timestamp");
        }
    }

    public static void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid payment ID");
        }
    }
}
