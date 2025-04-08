package com.elboutique.backend.service;

import com.elboutique.backend.DTO.response.UserResponse;
import com.elboutique.backend.mapper.UserMapper;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.UserRepository;
import com.elboutique.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    public User updateUser(Integer id, User updatedUser) {
        User user = getUserById(id);
        userMapper.updateUser(updatedUser, user);
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
