package com.adrater.datacollection.vo;

import java.util.Map;
import java.util.Set;


import com.mongodb.DBObject;

public class AdVO  {
	
	private String id;
	private String adHeader;
	private String adLink;
	private String adDetails;
	private CategoryVO subCategory;
	private LocationVO location;
	
	public AdVO(){
		
	}
	
	public AdVO(String adHeader, String adLink){
		this.adHeader = adHeader;
		this.adLink = adLink;
	}
	@Override
	public String toString(){
		
		return "{id:"+ this.id+ ", header:"+this.adHeader+" , link:"+this.adLink+" , details:"+this.adDetails+" , category:"+ this.subCategory+" , location:"+this.location+"}";
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdHeader() {
		return adHeader;
	}
	public void setAdHeader(String adHeader) {
		this.adHeader = adHeader;
	}
	public String getAdLink() {
		return adLink;
	}
	public void setAdLink(String adLink) {
		this.adLink = adLink;
	}
	public CategoryVO getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(CategoryVO subCategory) {
		this.subCategory = subCategory;
	}

	public String getAdDetails() {
		return adDetails;
	}

	public void setAdDetails(String adDetails) {
		this.adDetails = adDetails;
	}

	public LocationVO getLocation() {
		return location;
	}

	public void setLocation(LocationVO location) {
		this.location = location;
	}
		

}
