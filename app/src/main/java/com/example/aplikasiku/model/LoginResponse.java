package com.example.aplikasiku.model;

public class LoginResponse{
	private DataLogin data;
	private boolean success;
	private String message;

	public DataLogin getData() {
		return data;
	}

	public void setData(DataLogin data) {
		this.data = data;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}
