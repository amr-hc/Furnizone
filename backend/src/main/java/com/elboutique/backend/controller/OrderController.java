package com.elboutique.backend.controller;

import com.elboutique.backend.DTO.response.OrderResponse;
import com.elboutique.backend.model.Order;
import com.elboutique.backend.service.OrderService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) authentication.getCredentials();
        Integer userId = claims.get("id", Integer.class);
        Order order = orderService.createOrder(userId);
        return ResponseEntity.status(201).body(OrderResponse.fromEntity(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id, Authentication authentication) {
        return ResponseEntity.ok(orderService.getOrderById(id, authentication));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Integer id, @RequestBody Order order) {
        return ResponseEntity.ok(orderService.updateOrder(id, order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Integer id, Authentication authentication) {
        return ResponseEntity.ok(orderService.cancelOrder(id, authentication));
    }

    @PutMapping("/{id}/done")
    public ResponseEntity<Order> markOrderAsDone(@PathVariable Integer id, Authentication authentication) {
        return ResponseEntity.ok(orderService.markOrderAsDone(id, authentication));
    }
}
