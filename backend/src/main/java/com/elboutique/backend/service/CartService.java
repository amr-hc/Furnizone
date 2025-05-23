package com.elboutique.backend.service;

import com.elboutique.backend.DTO.response.CartResponse;
import com.elboutique.backend.mapper.ProductMapper;
import com.elboutique.backend.model.Cart;
import com.elboutique.backend.model.Product;
import com.elboutique.backend.model.User;
import com.elboutique.backend.repository.CartRepository;
import com.elboutique.backend.repository.ProductRepository;
import com.elboutique.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Find all cart items by user ID.
     */
    public List<CartResponse> findByUserId(Integer userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        List<CartResponse> response = cartItems.stream()
            .<CartResponse>map(cart -> CartResponse.builder()
                .id(cart.getId())
                .product(productMapper.toDto(cart.getProduct()))
                .quantity(cart.getQuantity())
                .build())
            .collect(Collectors.toList());

        return response;
    }

    /**
     * Add a product to the cart.
     */
    @Transactional
    public Cart addToCart(Integer userId, Integer productId, Integer quantity) {
        // Ensure the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the item already exists in the user's cart
        boolean existingCartItem = cartRepository.existsByUserIdAndProductId(userId, productId);
        if (existingCartItem) {
            throw new RuntimeException("This product is already in your cart");
        }

        // Create a new Cart instance dynamically
        Cart newCartItem = Cart.builder()
            .user(User.builder().id(userId).build())
            .product(product)
            .quantity(quantity)
            .build();

        // Save the cart item
        return cartRepository.save(newCartItem);
    }


    /**
     * Update the quantity of a cart item.
     */
    @Transactional
    public Cart updateCartItem(Integer cartId, Cart cartRequest) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        Product product = productRepository.findById(existingCart.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (product.getStock() < cartRequest.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        existingCart.setQuantity(cartRequest.getQuantity());
        return cartRepository.save(existingCart);
    }

    /**
     * Delete a cart item.
     */
    public void deleteCartItem(Integer cartId) {
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        cartRepository.delete(existingCart);
    }

    /**
     * Delete all cart items for a user.
     */
    @Transactional
    public void deleteAllCartItems(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }
        cartRepository.deleteByUserId(userId);
    }
}
