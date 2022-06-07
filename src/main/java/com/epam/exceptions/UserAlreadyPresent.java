package com.epam.exceptions;

public class UserAlreadyPresent extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyPresent(String message)
	{
		super(message);
	}
}
