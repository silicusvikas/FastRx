package com.parkwoodrx.fastrx.webservice;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData extends FastRxResponse {
	private String exceptionType;
	private String role;
	private long roleId;
	private String userId;
	private String username;
	private String corporationId;
	private String locationId;
	private String fullName;
	private String pharmacyName;
	private String firstLogin;

	public ResponseData() {
	}

	public ResponseData(String role, long roleid, String userid, String corporationId, String username,
			String locationId, String fullName ,String pharmacyName,String firstLogin) {
		this.role = role;
		this.userId = userid;
		this.roleId = roleid;
		this.corporationId = corporationId;
		this.username = username;
		this.locationId = locationId;
		this.fullName = fullName;
		this.pharmacyName = pharmacyName;
		this.firstLogin=firstLogin;
		setStatus(Status.SUCCESS);
	}
	
	public String getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(String corporationId) {
		this.corporationId = corporationId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPharmacyName() {
		return pharmacyName;
	}

	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}

}
