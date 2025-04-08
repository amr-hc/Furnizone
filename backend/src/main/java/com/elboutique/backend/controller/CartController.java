package com.elboutique.backend.controller;

import com.elboutique.backend.DTO.request.CartRequest;
import com.elboutique.backend.DTO.response.CartResponse;
import com.elboutique.backend.model.Cart;
import com.elboutique.backend.service.CartService;

import com.elboutique.backend.utilities.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final AuthUtils authUtils;
    /**
     * Get all cart items for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<CartResponse>> getUserCartItems() {
        Integer userId = authUtils.getAuthenticatedUser().getId();
        List<CartResponse> userCartItems = cartService.findByUserId(userId);
        return ResponseEntity.ok(userCartItems);
    }

    /**
     * Add a new item to the cart.
     */
    @PostMapping
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody CartRequest cartRequest) {
        Integer userId = authUtils.getAuthenticatedUser().getId();
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
