package com.example.aplikasiku.model;

import java.util.List;

public class UpdatePasswordResponse{
	private List<Object> data;
	private boolean success;
	private String message;

	public List<Object> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}