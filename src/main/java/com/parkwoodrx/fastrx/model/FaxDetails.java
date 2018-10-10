package com.parkwoodrx.fastrx.model;

import java.util.List;

public class FaxDetails {
	private List<String> faxJobIds;
	private String updatedBy;
	
	
	public List<String> getFaxJobIds() {
		return faxJobIds;
	}
	public void setFaxJobIds(List<String> faxJobIds) {
		this.faxJobIds = faxJobIds;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	

}
