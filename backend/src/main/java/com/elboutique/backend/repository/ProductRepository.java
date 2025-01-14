package com.elboutique.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
