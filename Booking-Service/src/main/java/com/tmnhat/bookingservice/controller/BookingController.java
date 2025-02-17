package com.tmnhat.bookingservice.controller;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.service.BookingService;
import com.tmnhat.bookingservice.service.Impl.BookingServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;


    public BookingController() {
        this.bookingService = new BookingServiceImpl();
    }

    @PostMapping()
    public ResponseEntity<String> saveBooking(@RequestBody Booking booking) throws SQLException {
        bookingService.saveBooking(booking);
        return ResponseEntity.ok("Booking saved successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBooking(@PathVariable("id") Long id,@RequestBody Booking booking) throws SQLException {
        bookingService.updateBooking(id,booking);
        return ResponseEntity.ok("Booking updated successfully");
    }
    @GetMapping()
    public ResponseEntity<List<Booking>> getAllBookings() throws SQLException {
        try {
            List<Booking> bookings = bookingService.getAllBookings();
            return ResponseEntity.ok(bookings);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("id") Long id) throws SQLException {
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
