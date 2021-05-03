package com.example.aplikasiku.model;

import java.util.List;

public class DataVolumeRatePerwaktu {
	private String total;
	private List<VolumePerwaktuItem> rate;

	public String getTotal(){
		return total;
	}

	public List<VolumePerwaktuItem> getRate(){
		return rate;
	}
}