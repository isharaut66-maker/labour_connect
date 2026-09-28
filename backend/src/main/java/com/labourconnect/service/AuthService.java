package com.labourconnect.service;

import com.labourconnect.dto.AuthResponse;
import com.labourconnect.dto.LoginRequest;
import com.labourconnect.dto.RegisterRequest;
import com.labourconnect.entity.User;
import com.labourconnect.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new AuthResponse(false, "Email is already registered", null);
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setRole(request.getRole().toUpperCase());
        
        // Hash password
        String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        savedUser.setPassword(null); // Don't return password

        return new AuthResponse(true, "Registration successful", savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (BCrypt.checkpw(request.getPassword(), user.getPassword())) {
                user.setPassword(null); // Don't return password
                return new AuthResponse(true, "Login successful", user);
            }
        }
        
        return new AuthResponse(false, "Invalid email or password", null);
    }
}
