package com.volvo.testvolvo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.volvo.testvolvo.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{

}