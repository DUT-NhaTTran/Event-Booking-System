package com.tmnhat.paymentservice.controller;

import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import com.tmnhat.paymentservice.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController() {
        this.paymentService = new PaymentServiceImpl();
    }
    @GetMapping("/status/{bookingId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable Long bookingId) {
        try {
            Payment payment = paymentService.getPaymentByBookingId(bookingId);
            if (payment != null) {
                return ResponseEntity.ok(payment);
            } else {
                return ResponseEntity.status(404).body("Booking not found for payment!");
            }
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error retrieving payment status: " + e.getMessage());
        }
    }
    @PostMapping()
    public ResponseEntity<String> savePayment(@RequestBody Payment payment) throws SQLException {
        paymentService.savePayment(payment);
        return ResponseEntity.ok("Payment saved successfully");
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updatePayment(@PathVariable("id") Long id, @RequestBody Payment payment) throws SQLException {
        paymentService.updatePayment(id,payment);
        return ResponseEntity.ok("Payment updated successfully");
    }
    @GetMapping()
    public ResponseEntity<List<Payment>> getAllPayments() throws SQLException {
        return ResponseEntity.ok().body(paymentService.getAllPayments());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") Long id) throws SQLException {
        return ResponseEntity.ok().body(paymentService.getPaymentById(id));
    }

}
