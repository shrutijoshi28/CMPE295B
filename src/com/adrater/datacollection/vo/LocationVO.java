package com.adrater.datacollection.vo;

public class LocationVO {
	
	private String lattitude;
	private String longitude;
	private String info;
	
		
	public LocationVO(){
		this.lattitude = null;
		this.longitude = null;
		this.info = null;
	}
	
	public LocationVO(String lattitude, String longitude){
		this.lattitude = lattitude;
		this.longitude = longitude;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString(){
		
		return "{info:"+this.info+" , lattitude:"+ this.lattitude +" , longitude:" + this.longitude+" }"; 
		
	}
	
}
