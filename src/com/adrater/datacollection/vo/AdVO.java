package com.adrater.datacollection.vo;

import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.beans.Field;


import com.mongodb.DBObject;

public class AdVO  {
	@Field("id")
	private String id;
	
	@Field("adHeader_txt")
	private String adHeader;
	
	@Field("url")
	private String adLink;
	
	@Field("adDetails_txt")
	private String adDetails;
	
	@Field("postDate_s")
	private String postDate;
	
	
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
		
		return "{id:"+ this.id+ ", header:"+this.adHeader+" , link:"+this.adLink+" , details:"+this.adDetails+" , time:"+this.postDate+ " , category:"+ this.subCategory+" , location:"+this.location+"}";
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

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public LocationVO getLocation() {
		return location;
	}

	public void setLocation(LocationVO location) {
		this.location = location;
	}
		

}
