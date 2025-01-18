package com.elboutique.backend.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.Order;
import com.elboutique.backend.model.User;

public interface OrderRepository extends JpaRepository<Order, Integer>{

    Collection<Order> findByUserAndStatusNot(User user, String string);

}
