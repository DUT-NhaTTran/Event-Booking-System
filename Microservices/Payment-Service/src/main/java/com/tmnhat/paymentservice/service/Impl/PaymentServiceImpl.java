package com.tmnhat.paymentservice.service.Impl;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.paymentservice.rabbitmq.RabbitMQProducer;
import com.tmnhat.paymentservice.repository.PaymentDAO;
import com.tmnhat.paymentservice.service.PaymentService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

public class PaymentServiceImpl implements PaymentService {
    private final PaymentDAO paymentDAO =
            new PaymentDAO();

    @Override
    public void savePayment(Payment payment) {
        try {
            paymentDAO.savePayment(payment);
        } catch (Exception e) {
            throw new DatabaseException("Error adding payment: " + e.getMessage());
        }
    }

    @Override
    public void updatePayment(Long id, Payment payment) {
        try {
            paymentDAO.updatePayment(id, payment);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Payment with ID " + id + " not found");
        } catch (Exception e) {
            throw new DatabaseException("Error updating Payment with ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        try {
            return paymentDAO.getAllPayments();
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving all payments");
        }
    }

    @Override
    public Payment getPaymentById(Long id) {
        try {
            return paymentDAO.getPaymentById(id);
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving Payment with ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public Payment getPaymentByBookingId(Long bookingId){
        try{
            return paymentDAO.getPaymentByBookingId(bookingId);
        }catch(Exception e){
            throw new DatabaseException("Error retrieving Payment with Booking ID " + bookingId + ": " + e.getMessage());
        }
    }

    @Override
    public void processPayment(Booking booking) {
        //boolean paymentSuccess = new Random().nextBoolean(); // Random thanh toán thành công/thất bại
//        String status = paymentSuccess ? "SUCCESS" : "FAILED";
        boolean paymentSuccess = true;
        String status = "SUCCESS";

        // Lưu thông tin thanh toán vào bảng `payment`
        Payment payment = new Payment.Builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())
                .amount(booking.getTicketCount() * 100.0) // Giả sử giá vé là 100
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        paymentDAO.savePayment(payment);

        // Gửi cập nhật trạng thái booking
        System.out.println("Gửi RabbitMQ");
        RabbitMQProducer.sendBookingUpdate(booking.getId(), status);

        if (paymentSuccess) {
            // Nếu thanh toán thành công, gửi cập nhật số vé
            RabbitMQProducer.sendEventUpdate(booking.getEventId(), booking.getTicketCount());
        }
    }
}
