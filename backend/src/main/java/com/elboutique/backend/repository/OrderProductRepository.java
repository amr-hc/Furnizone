package com.elboutique.backend.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
}
