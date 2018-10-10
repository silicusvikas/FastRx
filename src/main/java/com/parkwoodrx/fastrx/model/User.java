package com.parkwoodrx.fastrx.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String firstName;
	private String lastName;
	private String username;
	private String confirmPassword;
	private String phoneNumber;
	private String email;
	private String password;
	private long roleId;
	private String active;
	private String pharmacistLicenseNumber;
	private String stateLicenseNumber;
	private FastRxAudit fastRxAudit;
	private Timestamp firstLogin;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Timestamp getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Timestamp firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}

	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public String getPharmacistLicenseNumber() {
		return pharmacistLicenseNumber;
	}

	public void setPharmacistLicenseNumber(String pharmacistLicenseNumber) {
		this.pharmacistLicenseNumber = pharmacistLicenseNumber;
	}

	public String getStateLicenseNumber() {
		return stateLicenseNumber;
	}

	public void setStateLicenseNumber(String stateLicenseNumber) {
		this.stateLicenseNumber = stateLicenseNumber;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
