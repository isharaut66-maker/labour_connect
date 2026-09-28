package com.labourconnect.controller;

import com.labourconnect.entity.Project;
import com.labourconnect.entity.User;
import com.labourconnect.repository.ProjectRepository;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects(
            @RequestParam(required = false) Long clientId) {
        if (clientId != null) {
            return ResponseEntity.ok(projectRepository.findByClientId(clientId));
        }
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> clientOpt = userRepository.findById(userId);
        if (!clientOpt.isPresent()) return ResponseEntity.status(404).body("Client not found");

        project.setClient(clientOpt.get());
        if (project.getStatus() == null) {
            project.setStatus("OPEN");
        }

        Project saved = projectRepository.save(project);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) return ResponseEntity.ok(project.get());
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Project statusData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<Project> optProject = projectRepository.findById(id);
        if (optProject.isPresent()) {
            Project project = optProject.get();
            // Verify ownership
            if (!project.getClient().getId().equals(userId)) {
                return ResponseEntity.status(403).body("Forbidden");
            }
            project.setStatus(statusData.getStatus());
            return ResponseEntity.ok(projectRepository.save(project));
        }
        return ResponseEntity.notFound().build();
    }
}
