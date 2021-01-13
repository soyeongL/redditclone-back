package com.yeong.vege.exceptions;

public class PostNotFoundException extends RuntimeException{
	public PostNotFoundException(String message) {
		super(message);
	}
}
