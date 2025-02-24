package com.tmnhat.paymentservice.controller;

import com.tmnhat.common.payload.ResponseDataAPI;
import com.tmnhat.paymentservice.model.Payment;
import com.tmnhat.paymentservice.service.Impl.PaymentServiceImpl;
import com.tmnhat.paymentservice.service.PaymentService;
import com.tmnhat.paymentservice.validation.PaymentValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    // Lấy trạng thái thanh toán theo `bookingId`
    @GetMapping("/status/{bookingId}")
    public ResponseEntity<ResponseDataAPI> getPaymentStatus(@PathVariable Long bookingId) {
        PaymentValidator.validateId(bookingId); // Kiểm tra ID hợp lệ
        Payment payment = paymentService.getPaymentByBookingId(bookingId);

        if (payment != null) {
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(payment));
        } else {
            return ResponseEntity.status(404).body(ResponseDataAPI.error("Booking not found for payment!"));
        }
    }

    // Thêm thanh toán mới (Kiểm tra dữ liệu hợp lệ)
    @PostMapping()
    public ResponseEntity<ResponseDataAPI> savePayment(@RequestBody Payment payment) {
        PaymentValidator.validatePayment(payment); // Kiểm tra dữ liệu đầu vào
        paymentService.savePayment(payment);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }

    // Cập nhật thông tin thanh toán (Kiểm tra ID & dữ liệu hợp lệ)
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> updatePayment(@PathVariable("id") Long id, @RequestBody Payment payment) {
        PaymentValidator.validateId(id); // Kiểm tra ID hợp lệ
        PaymentValidator.validatePayment(payment); // Kiểm tra dữ liệu đầu vào
        paymentService.updatePayment(id, payment);
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMetaAndData());
    }

    @GetMapping()
    public ResponseEntity<ResponseDataAPI> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(payments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDataAPI> getPaymentById(@PathVariable("id") Long id) {
        PaymentValidator.validateId(id); // Kiểm tra ID hợp lệ
        Payment payment = paymentService.getPaymentById(id);

        if (payment != null) {
            return ResponseEntity.ok(ResponseDataAPI.successWithoutMeta(payment));
        } else {
            return ResponseEntity.status(404).body(ResponseDataAPI.error("Payment with ID " + id + " not found"));
        }
    }
}
