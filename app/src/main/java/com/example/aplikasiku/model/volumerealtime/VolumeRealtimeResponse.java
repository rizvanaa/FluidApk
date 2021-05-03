package com.example.aplikasiku.model.volumerealtime;

import java.util.List;

public class VolumeRealtimeResponse{
	private List<DataItem> data;
	private boolean success;
	private String message;

	public List<DataItem> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}