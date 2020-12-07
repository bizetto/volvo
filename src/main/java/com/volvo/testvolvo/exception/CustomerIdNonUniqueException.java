package com.volvo.testvolvo.exception;

public class CustomerIdNonUniqueException extends Exception{

	private static final long serialVersionUID = 1L;

	public CustomerIdNonUniqueException(Integer id) {
		super(id.toString());
	}
}