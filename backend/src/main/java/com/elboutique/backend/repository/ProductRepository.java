package com.elboutique.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findAll(Pageable pageable);

    List<Product> findByTitleContainingIgnoreCase(String title);
}
