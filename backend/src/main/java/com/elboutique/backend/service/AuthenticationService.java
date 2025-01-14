package com.elboutique.backend.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.LoginRequest;
import com.elboutique.backend.DTO.request.RegisterRequest;
import com.elboutique.backend.DTO.response.AuthenticationResponse;
import com.elboutique.backend.config.JwtService;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        String imagePath = "uploads/images/default.jpg";
        if (request.getImage() != null && !request.getImage().isEmpty()) {
            try {
                // Define the directory where images will be stored
                String uploadDir = "uploads/images/";
                // Create the directory if it doesn't exist
                Files.createDirectories(Paths.get(uploadDir));

                // Generate a unique filename
                String fileName = UUID.randomUUID().toString() + "_" + request.getImage().getOriginalFilename();

                // Save the file to the directory
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(request.getImage().getInputStream(), filePath);

                // Store the file path for saving in the database
                imagePath = filePath.toString();
            } catch (IOException e) {
                throw new RuntimeException("Failed to save image", e);
            }
        }

        var user = User.builder()
        .fullName(request.getFullName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .image(imagePath)
        .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken( request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse
            .builder()
            .token(jwtToken)
            .build();
    }

}
