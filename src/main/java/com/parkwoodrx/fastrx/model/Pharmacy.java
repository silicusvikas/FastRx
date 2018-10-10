package com.parkwoodrx.fastrx.model;



public class Pharmacy {

	
	private long id;
	
	private String name;
	
	public Pharmacy() {
		super();
	}
	
	public Pharmacy(String name){
		this.name = name;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
