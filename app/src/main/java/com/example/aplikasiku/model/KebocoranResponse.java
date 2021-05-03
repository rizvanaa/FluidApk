package com.example.aplikasiku.model;

import java.util.List;

public class KebocoranResponse{
	private List<DataKebocoran> data;
	private boolean success;
	private String message;

	public List<DataKebocoran> getData(){
		return data;
	}

	public boolean isSuccess(){
		return success;
	}

	public String getMessage(){
		return message;
	}
}