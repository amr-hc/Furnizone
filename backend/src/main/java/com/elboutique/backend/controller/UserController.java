package com.elboutique.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elboutique.backend.DTO.response.UserResponse;
import com.elboutique.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size ) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

}
