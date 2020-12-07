package com.volvo.testvolvo.exception;

public class ZipCodeParseException extends Exception{

	private static final long serialVersionUID = 1L;

	public ZipCodeParseException(String zipCode) {
		super(zipCode);
	}
}