package com.tmnhat.bookingservice.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.ConnectionFactory;
import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.rabbitmq.RabbitMQProducer;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingDAO extends BaseDAO {
    //Đặt vé

    public void saveBooking(Booking booking) {
        String saveBookingSQL = "INSERT INTO booking (booking_name, event_id, user_id, ticket_count, status, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            long bookingId = executeUpdateWithGeneratedKeys(saveBookingSQL, stmt -> {
                stmt.setString(1, booking.getBookingName());
                stmt.setLong(2, booking.getEventId());
                stmt.setLong(3, booking.getUserId());
                stmt.setInt(4, booking.getTicketCount());
                stmt.setString(5, "PENDING"); // Trạng thái mặc định
                stmt.setTimestamp(6, booking.getCreatedAt() != null
                        ? Timestamp.valueOf(booking.getCreatedAt())
                        : Timestamp.valueOf(LocalDateTime.now()));
            });

            if (bookingId > 0) {
                booking.setId(bookingId);
                // Booking được thêm thành công, tiến hành gửi message đến Payment Service
                try {
                    sendBookingToPayment(booking);
                } catch (Exception e) {
                    System.err.println("Failed to send booking to Payment Service: " + e.getMessage());
                    e.printStackTrace();
                    rollbackBooking(booking);
                }
            } else {
                throw new DatabaseException("Failed to save booking: No ID returned");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error saving booking: " + e.getMessage());
        }
    }
    private void rollbackBooking(Booking booking) {
        // Cập nhật trạng thái booking thành FAILED trong cơ sở dữ liệu
        try {
            String updateSQL = "UPDATE booking SET status = ? WHERE id = ?";
            // Thực hiện update trạng thái booking thành FAILED
            executeUpdate(updateSQL, stmt -> {
                stmt.setString(1, "FAILED");
                stmt.setLong(2, booking.getId());
            });
            System.out.println("Booking rolled back (status set to FAILED) for booking id: " + booking.getId());
        } catch (Exception e) {
            throw new DatabaseException("Error rolling back booking: " + e.getMessage());
        }
    }

    public void sendBookingToPayment(Booking booking) {
        try {
            // Gửi booking đến RabbitMQ thông qua RabbitMQProducer
            RabbitMQProducer.sendBookingToPayment(booking);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBooking(Long id, Booking booking) {
        String updateBooking = "UPDATE booking SET booking_name = ?, event_id= ?, user_id = ?, ticket_count = ?, status = ?, created_at = ? WHERE id = ?";
        int rowsAffected=executeUpdate(updateBooking, stmt -> {
            stmt.setString(1, booking.getBookingName());
            stmt.setObject(2, booking.getEventId());
            stmt.setObject(3, booking.getUserId());
            stmt.setInt(4, booking.getTicketCount());
            stmt.setString(5, booking.getStatus());
            // Kiểm tra null trước khi chuyển thành Timestamp
            stmt.setTimestamp(6, booking.getCreatedAt() != null ? Timestamp.valueOf(booking.getCreatedAt()) : null);
            stmt.setLong(7, id);
        });
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event with ID " + id + " not found");
        }
    }

    public Booking getBookingById(Long id) {
        String getBook = "SELECT * FROM booking WHERE id = ?";
        try {

            return executeQuery(getBook, stmt -> {
                stmt.setLong(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {

                    return new Booking.Builder()
                            .id(rs.getLong("id"))
                            .bookingName(rs.getString("booking_name"))
                            .eventId(rs.getLong("event_id"))
                            .userId(rs.getLong("user_id"))
                            .ticketCount(rs.getInt("ticket_count"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .build();

                }
                return null;
            });
        }catch (SQLException e) {
            throw new DatabaseException("Error fetching booking with ID " + id + ": " + e.getMessage());
        }
    }
    public List<Booking> getAllBookings(){

        String getAllBookings = "SELECT * FROM booking";
        try {
            return executeQuery(getAllBookings, stmt -> {
                List<Booking> bookingList = new ArrayList<>();
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {

                    Booking booking = new Booking.Builder()
                            .id(rs.getLong("id"))
                            .bookingName(rs.getString("booking_name"))
                            .eventId(rs.getLong("event_id"))
                            .userId(rs.getLong("user_id"))
                            .ticketCount(rs.getInt("ticket_count"))
                            .status(rs.getString("status"))
                            .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                            .build();

                    bookingList.add(booking);
                }
                return bookingList;
            });
        }
        catch (SQLException e) {
            throw new DatabaseException("Error fetching all bookings: " + e.getMessage());
        }
    }
    public void updateBookingStatus(Long id, String status) {

        String updateBookingStatus = "UPDATE booking SET status = ? WHERE id = ?";
        int rowAffected= executeUpdate(updateBookingStatus, stmt -> {
            stmt.setString(1, status);
            stmt.setLong(2, id);
        });
        if(rowAffected == 0) {
            throw new ResourceNotFoundException("Booking with ID " + id + " not found");
        }
    }

}
