package com.elboutique.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elboutique.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    Page<Product> findAllInStock(Pageable pageable);

    List<Product> findByTitleContainingIgnoreCase(String title);
}
