package com.tmnhat.bookingservice.repository;

import com.tmnhat.bookingservice.model.Booking;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO extends BaseDAO {
    //Đặt vé
    public void saveBooking(Booking booking) throws SQLException {
        String saveBooking = "INSERT INTO booking (booking_name,event_id,user_id,ticket_count,status,created_at) VALUES (?, ?, ?, ?, ?, ?)";
        executeUpdate(saveBooking, stmt -> {
            stmt.setString(1, booking.getBookingName());
            stmt.setLong(2, booking.getEventId());
            stmt.setLong(3, booking.getUserId());
            stmt.setInt(4, booking.getTicketCount());
            stmt.setString(5, booking.getStatus());
            // Kiểm tra null trước khi chuyển thành Timestamp
            stmt.setTimestamp(6, booking.getCreatedAt() != null ? Timestamp.valueOf(booking.getCreatedAt()) : null);
        });
    }
    public void updateBooking(Long id, Booking booking) throws SQLException {
        String updateBooking = "UPDATE booking SET booking_name = ?, event_id= ?, user_id = ?, ticket_count = ?, status = ?, created_at = ? WHERE id = ?";
        executeUpdate(updateBooking, stmt -> {
            stmt.setString(1, booking.getBookingName());
            stmt.setObject(2, booking.getEventId());
            stmt.setObject(3, booking.getUserId());
            stmt.setInt(4, booking.getTicketCount());
            stmt.setString(5, booking.getStatus());
            // Kiểm tra null trước khi chuyển thành Timestamp
            stmt.setTimestamp(6, booking.getCreatedAt() != null ? Timestamp.valueOf(booking.getCreatedAt()) : null);
            stmt.setLong(7, id);
        });
    }

    public Booking getBookingById(Long id) throws SQLException {
        String getBook = "SELECT * FROM booking WHERE id = ?";
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
    }
    public List<Booking> getAllBookings() throws SQLException {

        String getAllBookings = "SELECT * FROM booking";
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

}
