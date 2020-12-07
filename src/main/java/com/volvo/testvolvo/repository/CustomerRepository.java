package com.volvo.testvolvo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.volvo.testvolvo.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	@Query(value = "Select c from Customer c where c.id = COALESCE(:id, c.id) AND c.name = COALESCE(:name, c.name) AND c.age = COALESCE(:age, c.age) AND c.registrationDate = COALESCE(:registrationDate, c.registrationDate) and c.lastUpdateInfo = COALESCE(:lastUpdateInfo, c.lastUpdateInfo)", nativeQuery = false)
	List<Customer> findByProperties(@Param("id") Integer id, @Param("name") String name, @Param("age") Integer age, @Param("registrationDate") Date registrationDate, @Param("lastUpdateInfo") Date lastUpdateInfo);
}