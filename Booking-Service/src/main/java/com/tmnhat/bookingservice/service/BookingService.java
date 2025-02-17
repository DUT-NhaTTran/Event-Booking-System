package com.tmnhat.bookingservice.service;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.repository.BookingDAO;

import java.sql.SQLException;
import java.util.List;

public interface BookingService {

    void saveBooking(Booking booking) throws SQLException;
    void updateBooking(Long id,Booking booking) throws SQLException ;
    List<Booking> getAllBookings() throws SQLException;
    Booking getBookingById(Long id) throws SQLException ;
}
