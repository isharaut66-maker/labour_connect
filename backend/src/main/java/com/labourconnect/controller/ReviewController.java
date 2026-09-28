package com.labourconnect.controller;

import com.labourconnect.entity.Booking;
import com.labourconnect.entity.Review;
import com.labourconnect.entity.User;
import com.labourconnect.repository.BookingRepository;
import com.labourconnect.repository.ReviewRepository;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> reviewerOpt = userRepository.findById(userId);
        if (!reviewerOpt.isPresent()) return ResponseEntity.status(404).body("User not found");
        
        review.setReviewer(reviewerOpt.get());

        if (review.getBooking() != null && review.getBooking().getId() != null) {
            Optional<Booking> b = bookingRepository.findById(review.getBooking().getId());
            b.ifPresent(review::setBooking);
        }

        if (review.getLabourer() != null && review.getLabourer().getId() != null) {
            Optional<User> l = userRepository.findById(review.getLabourer().getId());
            l.ifPresent(review::setLabourer);
        }

        Review saved = reviewRepository.save(review);
        
        // TODO: Update labourer's average rating in LabourerProfile if needed
        
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/labourer/{id}")
    public ResponseEntity<List<Review>> getLabourerReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewRepository.findByLabourerId(id));
    }
}
