package com.volvo.testvolvo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.volvo.testvolvo.exception.CustomerIdInvalidException;
import com.volvo.testvolvo.exception.CustomerIdNonUniqueException;
import com.volvo.testvolvo.exception.ZipCodeEmptyException;
import com.volvo.testvolvo.exception.ZipCodeParseException;
import com.volvo.testvolvo.model.Customer;
import com.volvo.testvolvo.service.CustomerService;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@GetMapping
	public ResponseEntity<List<Customer>> listCustomers() {
		List<Customer> list = this.customerService.findAll();
		if (!list.isEmpty()) {
			return ResponseEntity.ok().body(list);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(value = "/search")
	public ResponseEntity<List<Customer>> listCustomers(@RequestParam(required = false, name = "id") Integer id,
			@RequestParam(required = false, name = "age") Integer age,
			@RequestParam(required = false, name = "name") String name,
			@RequestParam(required = false, name = "registrationDate") String strRegistrationDate,
			@RequestParam(required = false, name = "lastUpdateInfo") String strLastUpdateInfo) {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

		Date registrationDate = null;
		Date lastUpdateInfo = null;
		if (strRegistrationDate != null) {
			try {
				registrationDate = formatter.parse(strRegistrationDate);
			} catch (ParseException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
		if (strLastUpdateInfo != null) {
			try {
				lastUpdateInfo = formatter.parse(strLastUpdateInfo);
			} catch (ParseException e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		}
		
		List<Customer> list = this.customerService.findByProperties(id, name, age, registrationDate, lastUpdateInfo);
		if (!list.isEmpty()) {
			return ResponseEntity.ok().body(list);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> insert (@RequestBody Customer customer) {

		try {
			customer = this.customerService.saveCustomer(customer);
		} catch (ZipCodeEmptyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one address must be informed");
		} catch (ZipCodeParseException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The zip code " + e.getMessage() + " is invalid.");
		} catch (CustomerIdNonUniqueException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id " + e.getMessage() + " informed already exists.");
		}
			return ResponseEntity.ok(customer);

	}
	
	@PutMapping (value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Object> update (@PathVariable("id") Integer id, @RequestBody Customer customer) {
		try {
			customer = this.customerService.update(id, customer);
			return ResponseEntity.ok(customer);
		} catch (ZipCodeEmptyException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("At least one address must be informed");
		} catch (ZipCodeParseException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The zip code " + e.getMessage() + " is invalid.");
		} catch (CustomerIdInvalidException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The id " + e.getMessage() + " informed does not belog to the customer.");
		}
	}
	
	@DeleteMapping (value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Customer> delete (@PathVariable("id") Integer id) {
		try {
			this.customerService.deleteCustomer(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			
		}
		
	}
}