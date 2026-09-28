package com.labourconnect.controller;

import com.labourconnect.entity.JobApplication;
import com.labourconnect.entity.Project;
import com.labourconnect.entity.User;
import com.labourconnect.repository.JobApplicationRepository;
import com.labourconnect.repository.ProjectRepository;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    @Autowired
    private JobApplicationRepository applicationRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> applyForJob(@RequestBody JobApplication application, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> labourerOpt = userRepository.findById(userId);
        if (!labourerOpt.isPresent()) return ResponseEntity.status(404).body("User not found");

        if (application.getProject() == null || application.getProject().getId() == null) {
            return ResponseEntity.badRequest().body("Project ID required");
        }

        Optional<Project> projectOpt = projectRepository.findById(application.getProject().getId());
        if (!projectOpt.isPresent()) return ResponseEntity.notFound().build();

        application.setLabourer(labourerOpt.get());
        application.setProject(projectOpt.get());
        application.setStatus("PENDING");

        return ResponseEntity.ok(applicationRepository.save(application));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<JobApplication>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(applicationRepository.findByProjectId(projectId));
    }

    @GetMapping("/labourer/{labourerId}")
    public ResponseEntity<List<JobApplication>> getByLabourer(@PathVariable Long labourerId) {
        return ResponseEntity.ok(applicationRepository.findByLabourerId(labourerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody JobApplication statusData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<JobApplication> optApp = applicationRepository.findById(id);
        if (optApp.isPresent()) {
            JobApplication app = optApp.get();
            // In a real app we'd check if current user is the client of this project
            app.setStatus(statusData.getStatus());
            return ResponseEntity.ok(applicationRepository.save(app));
        }
        return ResponseEntity.notFound().build();
    }
}
