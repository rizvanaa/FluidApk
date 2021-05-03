package com.example.aplikasiku.model;

public class StatusButtonResponse{
	private DataStatusButton data;
	private boolean success;
	private String message;

	public DataStatusButton getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}
