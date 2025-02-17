package com.tmnhat.bookingservice.service.Impl;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.repository.BookingDAO;
import com.tmnhat.bookingservice.service.BookingService;

import java.sql.SQLException;
import java.util.List;

public class BookingServiceImpl implements BookingService {
    private BookingDAO bookingDao = new BookingDAO();
    @Override
    public void saveBooking(Booking booking) throws SQLException {
        bookingDao.saveBooking(booking);
    }
    @Override
    public void updateBooking(Long id,Booking booking) throws SQLException {
        bookingDao.updateBooking(id, booking);
    }
    @Override
    public List<Booking> getAllBookings() throws SQLException {
        return bookingDao.getAllBookings();
    }
    @Override
    public Booking getBookingById(Long id) throws SQLException {
        return bookingDao.getBookingById(id);
    }
}
