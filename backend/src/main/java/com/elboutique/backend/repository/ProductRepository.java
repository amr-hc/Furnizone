package com.elboutique.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByTitleContainingIgnoreCase(String title);
}
