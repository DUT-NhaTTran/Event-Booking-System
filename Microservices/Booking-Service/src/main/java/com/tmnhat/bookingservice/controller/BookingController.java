package com.tmnhat.bookingservice.controller;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.service.BookingService;
import com.tmnhat.bookingservice.service.Impl.BookingServiceImpl;
import com.tmnhat.bookingservice.validation.BookingValidator;
import com.tmnhat.common.payload.ResponseDataAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServiceImpl bookingService;
    @Autowired
    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    //Thêm đặt phòng mới (Kiểm tra dữ liệu hợp lệ)
    @PostMapping()
    public ResponseEntity<ResponseDataAPI> saveBooking(@RequestBody Booking booking) throws SQLException {
        BookingValidator.validateBooking(booking); // Kiểm tra dữ liệu đầu vào
        bookingService.saveBooking(booking);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }

    //Cập nhật đặt phòng (Kiểm tra ID & dữ liệu hợp lệ)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> updateBooking(@PathVariable("id") Long id, @RequestBody Booking booking) throws SQLException {
        BookingValidator.validateId(id); // Kiểm tra ID hợp lệ
        BookingValidator.validateBooking(booking); // Kiểm tra dữ liệu đầu vào
        bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }

    // Lấy danh sách tất cả booking
    @GetMapping()
    public ResponseEntity<ResponseDataAPI> getAllBookings() throws SQLException {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(bookings));
    }

    // Lấy thông tin booking theo ID (Kiểm tra ID hợp lệ)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> getBookingById(@PathVariable("id") Long id) throws SQLException {
        BookingValidator.validateId(id); // Kiểm tra ID hợp lệ
        Booking booking = bookingService.getBookingById(id);
        if (booking != null) {
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(booking));
        } else {
            return ResponseEntity.status(404).body(ResponseDataAPI.error("Booking with ID " + id + " not found"));
        }
    }
}
