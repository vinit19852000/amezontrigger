package com.example.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;



@Data
public class Mydata {

	
	@JsonProperty("url")
	String url;
	Double price;
	
	public Mydata() {
		
	}
	
	public String getUrl() {
		return this.url;
	}
	public Double getPrice() {
		return this.price;
	}
	public Mydata(String url,Double price) {
		this.url=url;
		this.price=price;
	}
}
