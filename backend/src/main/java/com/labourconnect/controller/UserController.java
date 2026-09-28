package com.labourconnect.controller;

import com.labourconnect.entity.User;
import com.labourconnect.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User safeUser = user.get();
            safeUser.setPassword(null);
            return ResponseEntity.ok(safeUser);
        }
        
        return ResponseEntity.status(404).body("User not found");
    }
}
