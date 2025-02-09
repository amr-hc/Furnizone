package com.elboutique.backend.service;

import com.elboutique.backend.DTO.response.UserResponse;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.UserRepository;
import com.elboutique.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, Math.min(size, 100));
        Page<User> users = userRepository.findAll(pageable);
        return users.map(user -> UserResponse.fromUser(user, this.baseUrl));
    }

    public User updateUser(Integer id, User updatedUser) {
        User user = getUserById(id); // Use the getUserById method to check existence
        user.setFullName(updatedUser.getFullName());
        user.setEmail(updatedUser.getEmail());
        user.setImage(updatedUser.getImage());
        user.setGender(updatedUser.getGender());
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
