package com.elboutique.backend.controller;

import com.elboutique.backend.DTO.request.CartRequest;
import com.elboutique.backend.DTO.response.CartResponse;
import com.elboutique.backend.model.Cart;
import com.elboutique.backend.service.CartService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * Get all cart items for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<CartResponse>> getUserCartItems() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) authentication.getCredentials();
        Integer userId = claims.get("id", Integer.class);
        List<CartResponse> userCartItems = cartService.findByUserId(userId);
        return ResponseEntity.ok(userCartItems);
    }

    /**
     * Add a new item to the cart.
     */
    @PostMapping
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody CartRequest cartRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Claims claims = (Claims) authentication.getCredentials();
        Integer userId = claims.get("id", Integer.class);
        Cart savedCart = cartService.addToCart(userId, cartRequest.getProduct_id(), cartRequest.getQuantity());
        return ResponseEntity.status(201).body(savedCart);
    }

    /**
     * Update the quantity of an item in the cart.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Integer id, @Valid @RequestBody Cart cartRequest) {
        Cart updatedCart = cartService.updateCartItem(id, cartRequest);
        return ResponseEntity.ok(updatedCart);
    }

    /**
     * Delete an item from the cart.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Integer id) {
        cartService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }

}
