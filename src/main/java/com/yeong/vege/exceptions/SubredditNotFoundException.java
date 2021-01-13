package com.yeong.vege.exceptions;

public class SubredditNotFoundException extends RuntimeException{
	public SubredditNotFoundException(String message) {
		super(message);
	}
}
