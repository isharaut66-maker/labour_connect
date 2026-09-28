package com.labourconnect.repository;

import com.labourconnect.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByClientId(Long clientId);
    List<Booking> findByLabourerId(Long labourerId);
    List<Booking> findByProjectId(Long projectId);
}
