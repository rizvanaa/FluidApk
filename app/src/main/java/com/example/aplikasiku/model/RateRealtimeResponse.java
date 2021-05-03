package com.example.aplikasiku.model;

import java.util.List;

public class RateRealtimeResponse{
	private List<DataRateRealtime> data;
	private boolean success;
	private String message;

	public List<DataRateRealtime> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}