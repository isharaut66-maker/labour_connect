package com.labourconnect.controller;

import com.labourconnect.entity.LabourerProfile;
import com.labourconnect.entity.User;
import com.labourconnect.repository.LabourerProfileRepository;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/labourers")
public class LabourerProfileController {

    @Autowired
    private LabourerProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<LabourerProfile>> getLabourers(
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String location) {
        
        List<LabourerProfile> profiles;
        if (skill != null || location != null) {
            profiles = profileRepository.searchLabourers(skill, location);
        } else {
            profiles = profileRepository.findAll();
        }
        
        // Simple matching score implementation
        for (LabourerProfile p : profiles) {
            // we can calculate a mock matching score dynamically
            // but for simplicity, we'll return the raw data and let frontend display a mock score if needed, 
            // or we could add a transient field to the entity.
        }
        
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLabourer(@PathVariable Long id) {
        Optional<LabourerProfile> profile = profileRepository.findById(id);
        if (profile.isPresent()) {
            return ResponseEntity.ok(profile.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/profile")
    public ResponseEntity<?> createOrUpdateProfile(@RequestBody LabourerProfile profileData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) return ResponseEntity.status(404).body("User not found");

        Optional<LabourerProfile> existing = profileRepository.findByUserId(userId);
        LabourerProfile profileToSave = existing.orElse(new LabourerProfile());
        
        profileToSave.setUser(userOpt.get());
        profileToSave.setSkill(profileData.getSkill());
        profileToSave.setExperience(profileData.getExperience());
        profileToSave.setDailyRate(profileData.getDailyRate());
        profileToSave.setLocation(profileData.getLocation());
        profileToSave.setAvailability(profileData.getAvailability());
        profileToSave.setBio(profileData.getBio());
        
        if (profileToSave.getRating() == null) {
            profileToSave.setRating(0.0);
        }

        LabourerProfile saved = profileRepository.save(profileToSave);
        return ResponseEntity.ok(saved);
    }
}
