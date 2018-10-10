package com.parkwoodrx.fastrx.model;

public class PharmacyLocation {

	private long id;
	private long corporationId;
	private String pharmacyName;
	private String stateLicenseNumber;
	private String ncpdpNumber;
	private String npiNumber;
	private String deaNumber;
	private String locationPin;
	private String address;
	private String city;
	private String state;
	private String zipcode;
	private String phoneNumber;
	private String faxNumber;
	private String active;
	private String pharmacyStoreNumber;
	private FastRxAudit fastRxAudit;
	/*For creating location admin*/
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private long roleId;
	private String failReason;
	private String addressSameAsCorp;
	
	
	public String getAddressSameAsCorp() {
		return addressSameAsCorp;
	}

	public void setAddressSameAsCorp(String addressSameAsCorp) {
		this.addressSameAsCorp = addressSameAsCorp;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPharmacyStoreNumber() {
		return pharmacyStoreNumber;
	}

	public void setPharmacyStoreNumber(String pharmacyStoreNumber) {
		this.pharmacyStoreNumber = pharmacyStoreNumber;
	}

	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}

	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getCorporationId() {
		return corporationId;
	}
	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}
	public String getPharmacyName() {
		return pharmacyName;
	}
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}
	public String getStateLicenseNumber() {
		return stateLicenseNumber;
	}
	public void setStateLicenseNumber(String stateLicenseNumber) {
		this.stateLicenseNumber = stateLicenseNumber;
	}
	public String getNcpdpNumber() {
		return ncpdpNumber;
	}
	public void setNcpdpNumber(String ncpdpNumber) {
		this.ncpdpNumber = ncpdpNumber;
	}
	public String getNpiNumber() {
		return npiNumber;
	}
	public void setNpiNumber(String npiNumber) {
		this.npiNumber = npiNumber;
	}
	
	public String getDeaNumber() {
		return deaNumber;
	}

	public void setDeaNumber(String deaNumber) {
		this.deaNumber = deaNumber;
	}

	public String getLocationPin() {
		return locationPin;
	}
	public void setLocationPin(String locationPin) {
		this.locationPin = locationPin;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@Override
	public String toString() {
		return "PharmacyLocation [id=" + id + ", corporationId=" + corporationId + ", pharmacyName=" + pharmacyName
				+ ", stateLicenseNumber=" + stateLicenseNumber + ", ncpdpNumber=" + ncpdpNumber + ", npiNumber="
				+ npiNumber + ", deaNumber=" + deaNumber + ", locationPin=" + locationPin + ", address=" + address
				+ ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", phoneNumber=" + phoneNumber
				+ ", faxNumber=" + faxNumber + ", active=" + active + ", pharmacyStoreNumber=" + pharmacyStoreNumber
				+ ", fastRxAudit=" + fastRxAudit + ", firstName=" + firstName + ", lastName=" + lastName + ", username="
				+ username + ", password=" + password + ", roleId=" + roleId + ", failReason=" + failReason + "]";
	}

}
