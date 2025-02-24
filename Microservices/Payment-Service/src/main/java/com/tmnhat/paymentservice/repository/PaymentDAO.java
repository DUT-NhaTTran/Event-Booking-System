package com.tmnhat.paymentservice.repository;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import com.tmnhat.paymentservice.model.Payment;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentDAO extends BaseDAO {
    public void savePayment(Payment payment) {
        String savePayment = "INSERT INTO payment (booking_id,user_id,amount,status,created_at) VALUES (?,?,?,?,?)";
        System.out.println("save payment " + payment);
        executeUpdate(savePayment, stmt -> {
            stmt.setLong(1, payment.getBookingId());
            stmt.setLong(2, payment.getUserId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getStatus());
            stmt.setTimestamp(5, payment.getCreatedAt() != null ? Timestamp.valueOf(payment.getCreatedAt()) : null);
        });


    }

    public void updatePayment(Long id, Payment payment) {
        String updatePayment = "UPDATE payment SET booking_id=?,user_id=?,amount=?,status=?,created_at=? WHERE id=?";
        int rowsAffected = executeUpdate(updatePayment, stmt -> {
            stmt.setLong(1, payment.getBookingId());
            stmt.setLong(2, payment.getUserId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getStatus());
            stmt.setTimestamp(5, payment.getCreatedAt() != null ? Timestamp.valueOf(payment.getCreatedAt()) : null);
            stmt.setLong(6, id);

        });
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");

        }
    }

    public List<Payment> getAllPayments() {
        String getAllPayments = "SELECT * FROM payment";
        try {

            return executeQuery(getAllPayments, stmt -> {
                List<Payment> paymentList = new ArrayList<>();
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Payment payment = new Payment.Builder()
                            .id(rs.getLong("id"))
                            .bookingId(rs.getLong("booking_id"))
                            .userId(rs.getLong("user_id"))
                            .amount(rs.getDouble("amount"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .build();
                    paymentList.add(payment);
                }
                return paymentList;
            });
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching all events: " + e.getMessage());
        }
    }

    public Payment getPaymentById(Long id) {

        String getPaymentById = "SELECT * FROM payment WHERE id=?";
        try {
            return executeQuery(getPaymentById, stmt -> {
                stmt.setLong(1, id);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new Payment.Builder()
                            .id(rs.getLong("id"))
                            .bookingId(rs.getLong("booking_id"))
                            .userId(rs.getLong("user_id"))
                            .amount(rs.getDouble("amount"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .build();
                }
                return null;
            });
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching event with ID " + id + ": " + e.getMessage());
        }
    }

    public Payment getPaymentByBookingId(Long bookingId) {
        String query = "SELECT * FROM payment WHERE booking_id = ?";
        try {

            return executeQuery(query, stmt -> {
                stmt.setLong(1, bookingId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    return new Payment.Builder()
                            .id(rs.getLong("id"))
                            .bookingId(rs.getLong("booking_id"))
                            .userId(rs.getLong("user_id"))
                            .amount(rs.getDouble("amount"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .build();
                }
                return null;
            });
        }
        catch (SQLException e) {
            throw new DatabaseException("Error fetching event with Booking ID " + bookingId + ": " + e.getMessage());
        }
    }

}
