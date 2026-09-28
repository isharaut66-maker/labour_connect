package com.labourconnect.controller;

import com.labourconnect.dto.AuthResponse;
import com.labourconnect.dto.LoginRequest;
import com.labourconnect.dto.RegisterRequest;
import com.labourconnect.entity.User;
import com.labourconnect.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpSession session) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            session.setAttribute("userId", response.getUser().getId());
            session.setAttribute("userRole", response.getUser().getRole());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request, HttpSession session) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            session.setAttribute("userId", response.getUser().getId());
            session.setAttribute("userRole", response.getUser().getRole());
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new AuthResponse(true, "Logged out successfully", null));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            // Usually we'd fetch from DB, but for simple demo, returning true is enough for session check
            return ResponseEntity.ok(new AuthResponse(true, "Authenticated", null));
        }
        return ResponseEntity.status(401).body(new AuthResponse(false, "Not authenticated", null));
    }
}
