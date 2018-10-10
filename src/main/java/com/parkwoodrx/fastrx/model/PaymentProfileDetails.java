package com.parkwoodrx.fastrx.model;

public class PaymentProfileDetails {

	private long id;
	private String firstName;
	private String lastName;
	private String company;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String phoneNumber;
	private String fax;
	private String email;
	private String cardNumber;
	private String expirationDate;
	private String cardCode;
	private String customerProfileId;
	private String customerPaymentProfileId;
	private long corporationId;
	private String cardHolderName;
	private String paymentType;
	
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	private FastRxAudit fastRxAudit;
	
	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}

	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	
	public long getCorporationId() {
		return corporationId;
	}

	public void setCorporationId(long corporationId) {
		this.corporationId = corporationId;
	}

	public String getCustomerProfileId() {
		return customerProfileId;
	}

	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}

	public String getCustomerPaymentProfileId() {
		return customerPaymentProfileId;
	}

	public void setCustomerPaymentProfileId(String customerPaymentProfileId) {
		this.customerPaymentProfileId = customerPaymentProfileId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}
	
	

}
