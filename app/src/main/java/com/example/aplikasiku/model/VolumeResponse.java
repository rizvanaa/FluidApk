package com.example.aplikasiku.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumeResponse{
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("success")
	@Expose
	private Boolean success;
	@SerializedName("data")
	@Expose
	private List<Datum> data = null;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public List<Datum> getData() {
		return data;
	}

	public void setData(List<Datum> data) {
		this.data = data;
	}

	public class Datum {

		@SerializedName("waktu")
		@Expose
		private String waktu;
		@SerializedName("vol")
		@Expose
		private String vol;

		public String getWaktu() {
			return waktu;
		}

		public void setWaktu(String waktu) {
			this.waktu = waktu;
		}

		public String getVol() {
			return vol;
		}

		public void setVol(String vol) {
			this.vol = vol;
		}

	}
}