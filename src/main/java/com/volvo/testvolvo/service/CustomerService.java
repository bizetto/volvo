package com.volvo.testvolvo.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volvo.testvolvo.exception.CustomerIdInvalidException;
import com.volvo.testvolvo.exception.CustomerIdNonUniqueException;
import com.volvo.testvolvo.exception.ZipCodeEmptyException;
import com.volvo.testvolvo.exception.ZipCodeParseException;
import com.volvo.testvolvo.model.Address;
import com.volvo.testvolvo.model.Customer;
import com.volvo.testvolvo.repository.CustomerRepository;

@Service	
public class CustomerService {
	
	private static final String ZIP_CODE_PATTERN = "^\\d{5}-\\d{3}$";
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public void deleteCustomer(Integer id) {
		this.customerRepository.deleteById(id);
	}
	
	public Customer saveCustomer(Customer customer) throws ZipCodeEmptyException, ZipCodeParseException, CustomerIdNonUniqueException {
		validateZipCodePattern(customer.getAddresses());
		if (this.customerRepository.findById(customer.getId()).isPresent()) {
			throw new CustomerIdNonUniqueException(customer.getId());
		}
		return this.customerRepository.saveAndFlush(customer);
	}
	
	public Customer update(Integer id, Customer customer) throws ZipCodeEmptyException, ZipCodeParseException, CustomerIdInvalidException {
		if (!id.equals(customer.getId())) {
			throw new CustomerIdInvalidException(id);
		}
		validateZipCodePattern(customer.getAddresses());
		return this.customerRepository.saveAndFlush(customer);
	}
	
	private boolean validateZipCodePattern(List<Address> list) throws ZipCodeEmptyException, ZipCodeParseException {
		
		if (list == null || list.isEmpty()) {
			throw new ZipCodeEmptyException();
		}
		
		List<Address> result = list.stream().filter(add -> !add.getZipCode().matches(ZIP_CODE_PATTERN)).collect(Collectors.toList());

		if (!result.isEmpty()) {
			throw new ZipCodeParseException(result.get(0).getZipCode());
		}
		
		return true;
	}
	
	public List<Customer> findAll() {
		return this.customerRepository.findAll();
	}
	
	public List<Customer> findByProperties(Integer id, String name, Integer age, Date registrationDate, Date lastUpdateInfo) {
		return this.customerRepository.findByProperties(id, name, age, registrationDate, lastUpdateInfo);
	}
}