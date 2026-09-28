package com.labourconnect.controller;

import com.labourconnect.entity.Booking;
import com.labourconnect.entity.Payment;
import com.labourconnect.repository.BookingRepository;
import com.labourconnect.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody Payment paymentReq, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        if (paymentReq.getBooking() == null || paymentReq.getBooking().getId() == null) {
            return ResponseEntity.badRequest().body("Booking ID required");
        }

        Optional<Booking> optBooking = bookingRepository.findById(paymentReq.getBooking().getId());
        if (!optBooking.isPresent()) return ResponseEntity.notFound().build();

        Booking booking = optBooking.get();
        // Generate fake transaction ID for demo
        paymentReq.setTransactionId("LC-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        paymentReq.setBooking(booking);
        paymentReq.setStatus("SUCCESS");

        Payment saved = paymentRepository.save(paymentReq);
        
        // Update booking status if needed
        booking.setStatus("COMPLETED");
        bookingRepository.save(booking);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayment(@PathVariable Long id) {
        Optional<Payment> p = paymentRepository.findById(id);
        if (p.isPresent()) return ResponseEntity.ok(p.get());
        return ResponseEntity.notFound().build();
    }
}
