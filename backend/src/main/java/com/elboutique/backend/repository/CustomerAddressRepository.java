package com.elboutique.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elboutique.backend.model.CustomerAddress;

public interface CustomerAddressRepository extends JpaRepository<CustomerAddress, Integer>{

}
