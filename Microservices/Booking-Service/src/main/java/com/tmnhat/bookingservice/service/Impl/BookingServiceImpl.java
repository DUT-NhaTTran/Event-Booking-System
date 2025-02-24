package com.tmnhat.bookingservice.service.Impl;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.repository.BookingDAO;
import com.tmnhat.bookingservice.service.BookingService;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingDAO bookingDao;

    @Override
    public void saveBooking(Booking booking) {
        try {
            bookingDao.saveBooking(booking);

        } catch (Exception e) {
            throw new DatabaseException("Error adding Booking: " + e.getMessage());
        }
    }

    @Override
    public void updateBooking(Long id, Booking booking) {
        try {
            bookingDao.updateBooking(id, booking);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Booking with ID " + id + " not found");
        } catch (Exception e) {
            throw new DatabaseException("Error updating booking with ID " + id + ": " + e.getMessage());
        }
    }

    @Override
    public List<Booking> getAllBookings() {
        try {
            return bookingDao.getAllBookings();
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving bookings: " + e.getMessage());
        }
    }

    @Override
    public Booking getBookingById(Long id) {
        try {
            return bookingDao.getBookingById(id);
        } catch (Exception e) {
            throw new DatabaseException("Error retrieving events: " + e.getMessage());
        }
    }
}
