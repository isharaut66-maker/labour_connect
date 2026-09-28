package com.labourconnect.repository;

import com.labourconnect.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    List<JobApplication> findByProjectId(Long projectId);
    List<JobApplication> findByLabourerId(Long labourerId);
}
