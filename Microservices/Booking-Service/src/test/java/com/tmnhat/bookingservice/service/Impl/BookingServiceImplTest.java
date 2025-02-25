package com.tmnhat.bookingservice.service.Impl;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.bookingservice.repository.BookingDAO;
import com.tmnhat.bookingservice.service.Impl.BookingServiceImpl;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingDAO bookingDao;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void testSaveBooking_HappyPath() {
        Booking booking = new Booking.Builder().build();
        bookingService.saveBooking(booking);
        verify(bookingDao, times(1)).saveBooking(booking);
    }

    @Test
    public void testSaveBooking_Exception() {
        Booking booking = new Booking.Builder().build();
        doThrow(new RuntimeException("DB error")).when(bookingDao).saveBooking(booking);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            bookingService.saveBooking(booking);
        });
        assertTrue(exception.getMessage().contains("Error adding Booking"));
    }

    @Test
    public void testUpdateBooking_HappyPath() {
        Booking booking = new Booking.Builder().build();
        Long id = 1L;
        bookingService.updateBooking(id, booking);
        verify(bookingDao, times(1)).updateBooking(id, booking);
    }

    @Test
    public void testUpdateBooking_ResourceNotFound() {
        Booking booking = new Booking.Builder().build();
        Long id = 1L;
        doThrow(new ResourceNotFoundException("Not found")).when(bookingDao).updateBooking(id, booking);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.updateBooking(id, booking);
        });
        assertTrue(exception.getMessage().contains("Booking with ID " + id + " not found"));
    }

    @Test
    public void testUpdateBooking_OtherException() {
        Booking booking = new Booking.Builder().build();
        Long id = 1L;
        doThrow(new RuntimeException("Other error")).when(bookingDao).updateBooking(id, booking);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            bookingService.updateBooking(id, booking);
        });
        assertTrue(exception.getMessage().contains("Error updating booking with ID " + id));
    }

    @Test
    public void testGetAllBookings_HappyPath() {
        Booking booking1 = new Booking.Builder().build();

        Booking booking2 = new Booking.Builder().build();
        List<Booking> bookings = Arrays.asList(booking1, booking2);
        when(bookingDao.getAllBookings()).thenReturn(bookings);
        List<Booking> result = bookingService.getAllBookings();
        assertEquals(2, result.size());
        verify(bookingDao, times(1)).getAllBookings();
    }

    @Test
    public void testGetAllBookings_Exception() {
        when(bookingDao.getAllBookings()).thenThrow(new RuntimeException("DB error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            bookingService.getAllBookings();
        });
        assertTrue(exception.getMessage().contains("Error retrieving bookings"));
    }

    @Test
    public void testGetBookingById_HappyPath() {
        Booking booking = new Booking.Builder().build();
        Long id = 1L;
        when(bookingDao.getBookingById(id)).thenReturn(booking);
        Booking result = bookingService.getBookingById(id);
        assertNotNull(result);
        verify(bookingDao, times(1)).getBookingById(id);
    }

    @Test
    public void testGetBookingById_Exception() {
        Long id = 1L;
        when(bookingDao.getBookingById(id)).thenThrow(new RuntimeException("DB error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            bookingService.getBookingById(id);
        });
        assertTrue(exception.getMessage().contains("Error retrieving events"));
    }
}
