package com.tmnhat.paymentservice.service.Impl;

import com.tmnhat.bookingservice.model.Booking;
import com.tmnhat.common.exception.DatabaseException;
import com.tmnhat.common.exception.ResourceNotFoundException;
import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.paymentservice.payload.PaymentStatus;
import com.tmnhat.paymentservice.rabbitmq.RabbitMQProducer;
import com.tmnhat.paymentservice.repository.PaymentDAO;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private PaymentDAO paymentDAO;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    // Helper method để tạo Payment thông qua builder pattern
    private Payment createPayment() {
        return new Payment.Builder()
                .build();
    }

    @Test
    public void testSavePayment_HappyPath() {
        Payment payment = createPayment();
        paymentService.savePayment(payment);
        verify(paymentDAO, times(1)).savePayment(payment);
    }

    @Test
    public void testSavePayment_Exception() {
        Payment payment = createPayment();
        doThrow(new RuntimeException("DB error")).when(paymentDAO).savePayment(payment);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            paymentService.savePayment(payment);
        });
        assertTrue(exception.getMessage().contains("Error adding payment"));
    }

    @Test
    public void testUpdatePayment_HappyPath() {
        Payment payment = createPayment();
        Long id = 1L;
        paymentService.updatePayment(id, payment);
        verify(paymentDAO, times(1)).updatePayment(id, payment);
    }

    @Test
    public void testUpdatePayment_ResourceNotFound() {
        Payment payment = createPayment();
        Long id = 1L;
        doThrow(new ResourceNotFoundException("Not found")).when(paymentDAO).updatePayment(id, payment);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.updatePayment(id, payment);
        });
        assertTrue(exception.getMessage().contains("Payment with ID " + id + " not found"));
    }

    @Test
    public void testUpdatePayment_OtherException() {
        Payment payment = createPayment();
        Long id = 1L;
        doThrow(new RuntimeException("Other error")).when(paymentDAO).updatePayment(id, payment);
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            paymentService.updatePayment(id, payment);
        });
        assertTrue(exception.getMessage().contains("Error updating Payment with ID " + id));
    }

    @Test
    public void testGetAllPayments_HappyPath() {
        Payment p1 = createPayment();
        Payment p2 = createPayment();
        List<Payment> payments = Arrays.asList(p1, p2);
        when(paymentDAO.getAllPayments()).thenReturn(payments);
        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
        verify(paymentDAO, times(1)).getAllPayments();
    }

    @Test
    public void testGetAllPayments_Exception() {
        when(paymentDAO.getAllPayments()).thenThrow(new RuntimeException("DB error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            paymentService.getAllPayments();
        });
        assertTrue(exception.getMessage().contains("Error retrieving all payments"));
    }

    @Test
    public void testGetPaymentById_HappyPath() {
        Payment payment = createPayment();
        Long id = 1L;
        when(paymentDAO.getPaymentById(id)).thenReturn(payment);
        Payment result = paymentService.getPaymentById(id);
        assertNotNull(result);
        verify(paymentDAO, times(1)).getPaymentById(id);
    }

    @Test
    public void testGetPaymentById_Exception() {
        Long id = 1L;
        when(paymentDAO.getPaymentById(id)).thenThrow(new RuntimeException("DB error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            paymentService.getPaymentById(id);
        });
        assertTrue(exception.getMessage().contains("Error retrieving Payment with ID " + id));
    }

    @Test
    public void testGetPaymentByBookingId_HappyPath() {
        Payment payment = createPayment();
        Long bookingId = 10L;
        when(paymentDAO.getPaymentByBookingId(bookingId)).thenReturn(payment);
        Payment result = paymentService.getPaymentByBookingId(bookingId);
        assertNotNull(result);
        verify(paymentDAO, times(1)).getPaymentByBookingId(bookingId);
    }

    @Test
    public void testGetPaymentByBookingId_Exception() {
        Long bookingId = 10L;
        when(paymentDAO.getPaymentByBookingId(bookingId)).thenThrow(new RuntimeException("DB error"));
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            paymentService.getPaymentByBookingId(bookingId);
        });
        assertTrue(exception.getMessage().contains("Error retrieving Payment with Booking ID " + bookingId));
    }

    @Test
    public void testProcessPayment_HappyPath() throws InterruptedException {
        Booking booking = new Booking.Builder().build();
        booking.setId(1L);
        booking.setUserId(100L);
        booking.setTicketCount(2);
        booking.setEventId(50L);

        try (MockedStatic<RabbitMQProducer> rabbitMock = Mockito.mockStatic(RabbitMQProducer.class)) {
            paymentService.processPayment(booking);
            // Chờ đủ thời gian để thread xử lý
            Thread.sleep(6000);

            // Xác minh rằng paymentDAO.savePayment được gọi với đối tượng Payment có giá trị đúng
            ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
            verify(paymentDAO, times(1)).savePayment(paymentCaptor.capture());
            Payment capturedPayment = paymentCaptor.getValue();
            assertEquals(booking.getId(), capturedPayment.getBookingId());
            assertEquals(booking.getUserId(), capturedPayment.getUserId());
            assertEquals(booking.getTicketCount() * 100.0, capturedPayment.getAmount());
            assertEquals(PaymentStatus.SUCCESS.toString(), capturedPayment.getStatus());
            assertNotNull(capturedPayment.getCreatedAt());

            rabbitMock.verify(() -> RabbitMQProducer.sendBookingUpdate(booking.getId(), PaymentStatus.SUCCESS.toString()), times(1));
            rabbitMock.verify(() -> RabbitMQProducer.sendEventUpdate(booking.getEventId(), booking.getTicketCount()), times(1));
        }
    }
}
