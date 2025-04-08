package com.elboutique.backend.service;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.LoginRequest;
import com.elboutique.backend.DTO.request.RegisterRequest;
import com.elboutique.backend.DTO.response.AuthenticationResponse;
import com.elboutique.backend.config.JwtService;
import com.elboutique.backend.mapper.UserMapper;
import com.elboutique.backend.model.Admin;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.AdminRepository;
import com.elboutique.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        String imagePath = "uploads/images/user/default.png";

        if (registerRequest.getImage() != null && !registerRequest.getImage().isEmpty()) {
            imagePath = fileStorageService.saveFile(registerRequest.getImage(), "user");
        }

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setImage(imagePath);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user, "User");
        return AuthenticationResponse
            .builder()
            .accessToken(jwtToken)
            .user(userMapper.toDto(user))
            .role("User")
            .build();
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        // Authenticate user or admin
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Check if the user is in the `users` table
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String jwtToken = jwtService.generateToken(user, "User");
            return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(userMapper.toDto(user))
                .role("User")
                .build();
        }

        // If not found, check the `admins` table
        Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            String jwtToken = jwtService.generateToken(admin, "Admin");
            return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(userMapper.adminToDto(admin))
                .role("Admin")
                .build();
        }

        throw new UsernameNotFoundException("Invalid credentials");

    }

}
