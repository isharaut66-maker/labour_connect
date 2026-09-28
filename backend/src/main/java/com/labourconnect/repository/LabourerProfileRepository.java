package com.labourconnect.repository;

import com.labourconnect.entity.LabourerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface LabourerProfileRepository extends JpaRepository<LabourerProfile, Long> {
    Optional<LabourerProfile> findByUserId(Long userId);
    
    @Query("SELECT p FROM LabourerProfile p WHERE " +
           "(:skill IS NULL OR LOWER(p.skill) LIKE LOWER(CONCAT('%', :skill, '%'))) AND " +
           "(:location IS NULL OR LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    List<LabourerProfile> searchLabourers(@Param("skill") String skill, @Param("location") String location);
}
