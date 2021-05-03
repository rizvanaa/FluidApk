package com.example.aplikasiku.model;

import java.util.List;

public class RateResponse {
	private List<DataRate> data;
	private boolean success;
	private String message;

	public List<DataRate> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}