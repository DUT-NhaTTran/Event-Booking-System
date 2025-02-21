package com.tmnhat.paymentservice.service;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.paymentservice.model.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    void savePayment(Payment payment);

    void updatePayment(Long id, Payment payment);

    List<Payment> getAllPayments();

    Payment getPaymentById(Long id);

    Payment getPaymentByBookingId(Long id);

    void processPayment(Booking booking);
}
