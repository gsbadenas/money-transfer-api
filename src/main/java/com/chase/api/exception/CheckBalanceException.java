package com.chase.api.exception;

public class CheckBalanceException extends SystemException {

	public CheckBalanceException(String message, String errorCode) {
		super(message, errorCode);
	}

}
