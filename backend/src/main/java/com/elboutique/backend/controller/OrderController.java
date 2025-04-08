package com.elboutique.backend.controller;

import com.elboutique.backend.DTO.response.OrderResponse;
import com.elboutique.backend.model.Order;
import com.elboutique.backend.service.OrderService;
import com.elboutique.backend.utilities.AuthUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthUtils authUtils;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user")
    public ResponseEntity<List<OrderResponse>> getUserOrders(Authentication authentication) {
        return ResponseEntity.ok(orderService.getUserOrders(authentication));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder() {
        Integer userId = authUtils.getAuthenticatedUser().getId();
        Order order = orderService.createOrder(userId);
        return ResponseEntity.status(201).body(OrderResponse.fromEntity(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id, Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrderById(id, authentication));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        return ResponseEntity.ok(OrderResponse.fromEntity(orderService.updateOrder(id, order)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Integer id, Authentication authentication) {
        orderService.deleteOrder(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Integer id, Authentication authentication) {
        Order order = orderService.cancelOrder(id, authentication);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }

    @PutMapping("/{id}/done")
    public ResponseEntity<OrderResponse> markOrderAsDone(@PathVariable Integer id) {
        Order order = orderService.markOrderAsDone(id);
        return ResponseEntity.ok(OrderResponse.fromEntity(order));
    }
}
