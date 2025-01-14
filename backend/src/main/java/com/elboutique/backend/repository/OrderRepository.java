package com.elboutique.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
