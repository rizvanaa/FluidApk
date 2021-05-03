package com.example.aplikasiku.model;

public class RealtimeResponse {
	private DataRealtime data;
	private boolean success;
	private String message;

	public DataRealtime getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}
