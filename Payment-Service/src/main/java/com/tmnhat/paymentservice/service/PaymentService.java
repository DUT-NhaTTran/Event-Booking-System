package com.tmnhat.paymentservice.service;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.paymentservice.model.Payment;

import java.sql.SQLException;
import java.util.List;

public interface PaymentService {
    void savePayment(Payment payment) throws SQLException;

    void updatePayment(Long id, Payment payment) throws SQLException;

    List<Payment> getAllPayments() throws SQLException;

    Payment getPaymentById(Long id) throws SQLException;

    Payment getPaymentByBookingId(Long id) throws SQLException;

    void processPayment(Booking booking) throws SQLException;
}
