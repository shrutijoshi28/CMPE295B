package com.adrater.datacollection.vo;

public class CategoryVO {

	private String category;
	private String link;
	
	public CategoryVO(){
		
	}
	
	public CategoryVO( String category, String link){
		
		this.category = category;
		this.link = link;
		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public String toString(){
		
		return "{category:"+this.category+" , link:"+ this.link+"}";
	}
	
}
