package com.labourconnect.controller;

import com.labourconnect.entity.Booking;
import com.labourconnect.entity.Project;
import com.labourconnect.entity.User;
import com.labourconnect.repository.BookingRepository;
import com.labourconnect.repository.ProjectRepository;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> clientOpt = userRepository.findById(userId);
        if (!clientOpt.isPresent()) return ResponseEntity.status(404).body("Client not found");

        booking.setClient(clientOpt.get());
        
        if (booking.getProject() != null && booking.getProject().getId() != null) {
            Optional<Project> p = projectRepository.findById(booking.getProject().getId());
            p.ifPresent(booking::setProject);
        }

        if (booking.getLabourer() != null && booking.getLabourer().getId() != null) {
            Optional<User> l = userRepository.findById(booking.getLabourer().getId());
            l.ifPresent(booking::setLabourer);
        }

        // Calculate total amount
        if (booking.getStartDate() != null && booking.getEndDate() != null && booking.getRate() != null) {
            long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;
            booking.setTotalAmount(days * booking.getRate());
        }

        booking.setStatus("PENDING");
        return ResponseEntity.ok(bookingRepository.save(booking));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getBookings(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        if (userId == null) return ResponseEntity.status(401).build();

        if ("LABOURER".equalsIgnoreCase(role)) {
            return ResponseEntity.ok(bookingRepository.findByLabourerId(userId));
        } else {
            return ResponseEntity.ok(bookingRepository.findByClientId(userId));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBooking(@PathVariable Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isPresent()) return ResponseEntity.ok(booking.get());
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Booking statusData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<Booking> opt = bookingRepository.findById(id);
        if (opt.isPresent()) {
            Booking booking = opt.get();
            booking.setStatus(statusData.getStatus());
            return ResponseEntity.ok(bookingRepository.save(booking));
        }
        return ResponseEntity.notFound().build();
    }
}
