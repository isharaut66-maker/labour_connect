package com.labourconnect.repository;

import com.labourconnect.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByLabourerId(Long labourerId);
}
