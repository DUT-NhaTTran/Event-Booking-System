package com.tmnhat.bookingservice.service;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.repository.BookingDAO;

import java.sql.SQLException;
import java.util.List;

public interface BookingService {

    void saveBooking(Booking booking);
    void updateBooking(Long id,Booking booking) ;
    List<Booking> getAllBookings() ;
    Booking getBookingById(Long id) ;
}
