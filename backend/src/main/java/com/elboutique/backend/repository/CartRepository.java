package com.elboutique.backend.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    /**
     * Find all cart items by user ID.
     */
    List<Cart> findByUserId(Integer userId);

    /**
     * Check if a product is already in the user's cart.
     */
    boolean existsByUserIdAndProductId(Integer userId, Integer productId);

    /**
     * Delete all cart items for a specific user.
     */
    void deleteByUserId(Integer userId);

}
