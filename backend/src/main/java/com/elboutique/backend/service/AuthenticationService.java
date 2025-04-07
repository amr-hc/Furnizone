package com.elboutique.backend.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elboutique.backend.DTO.request.LoginRequest;
import com.elboutique.backend.DTO.request.RegisterRequest;
import com.elboutique.backend.DTO.response.AuthenticationResponse;
import com.elboutique.backend.DTO.response.UserResponse;
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

    public AuthenticationResponse register(RegisterRequest request) {
        String imagePath = "uploads/images/user/default.png";

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            imagePath = fileStorageService.saveFile(request.getImage(), "user");
        }

        User user = User.builder()
        .fullName(request.getFull_name())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .image(imagePath)
        .gender(request.getGender())
        .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user, "User");
        return AuthenticationResponse
            .builder()
            .accessToken(jwtToken)
            .user(UserResponse.builder().id(user.getId()).full_name(user.getFullName()).email(user.getEmail()).image_url(user.getImage()).gender(user.getGender()).build())
            .role("User")
            .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        // Authenticate user or admin
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Check if the user is in the `users` table
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
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
        Optional<Admin> adminOptional = adminRepository.findByEmail(request.getEmail());
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            String jwtToken = jwtService.generateToken(admin, "Admin");
            return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .user(
                    UserResponse.builder().id(admin.getId()).full_name(admin.getName()).email(admin.getEmail()).image_url(admin.getImage()).build()
                )
                .role("Admin")
                .build();
        }

        throw new UsernameNotFoundException("Invalid credentials");

    }

}
