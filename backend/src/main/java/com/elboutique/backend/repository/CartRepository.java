package com.elboutique.backend.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
