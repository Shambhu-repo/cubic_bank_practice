package com.rab3tech.service.exception;

/**
 * 
 * 
 *
 */
public class BankServiceException extends RuntimeException{
	
	public BankServiceException(String message) {
		super(message);
	}
	
	public BankServiceException(String message,Throwable throwable) {
		super(message,throwable);
	}

}
