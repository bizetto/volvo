package com.volvo.testvolvo.exception;

public class CustomerIdInvalidException extends Exception{

	private static final long serialVersionUID = 1L;

	public CustomerIdInvalidException(Integer id) {
		super(id.toString());
	}
}