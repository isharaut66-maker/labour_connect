package com.labourconnect.controller;

import com.labourconnect.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private JobApplicationRepository applicationRepository;

    @GetMapping
    public ResponseEntity<?> getDashboardStats(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Map<String, Object> stats = new HashMap<>();
        
        if ("CLIENT".equalsIgnoreCase(role) || "CONTRACTOR".equalsIgnoreCase(role) || "ORGANIZATION".equalsIgnoreCase(role)) {
            stats.put("totalProjects", projectRepository.findByClientId(userId).size());
            stats.put("activeBookings", bookingRepository.findByClientId(userId).stream()
                    .filter(b -> !"COMPLETED".equals(b.getStatus()) && !"CANCELLED".equals(b.getStatus())).count());
            stats.put("completedBookings", bookingRepository.findByClientId(userId).stream()
                    .filter(b -> "COMPLETED".equals(b.getStatus())).count());
        } else if ("LABOURER".equalsIgnoreCase(role)) {
            stats.put("totalApplications", applicationRepository.findByLabourerId(userId).size());
            stats.put("activeBookings", bookingRepository.findByLabourerId(userId).stream()
                    .filter(b -> !"COMPLETED".equals(b.getStatus()) && !"CANCELLED".equals(b.getStatus())).count());
            stats.put("completedBookings", bookingRepository.findByLabourerId(userId).stream()
                    .filter(b -> "COMPLETED".equals(b.getStatus())).count());
        } else {
            // Admin or other
            stats.put("totalProjects", projectRepository.count());
            stats.put("totalBookings", bookingRepository.count());
        }

        return ResponseEntity.ok(stats);
    }
}
