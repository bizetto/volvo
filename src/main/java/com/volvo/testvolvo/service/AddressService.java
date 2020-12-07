package com.volvo.testvolvo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.volvo.testvolvo.model.Address;
import com.volvo.testvolvo.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	private AddressRepository addressRepository;
	
	public Address findById(String zipCode) {
		Optional<Address> optional = this.addressRepository.findById(zipCode);
		
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public Address save(Address address) {
		return this.addressRepository.save(address);
	}
}