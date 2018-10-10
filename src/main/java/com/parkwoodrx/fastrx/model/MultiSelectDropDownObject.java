package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class MultiSelectDropDownObject  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	private String itemName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	
}
