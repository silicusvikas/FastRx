package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class PharmacyCorporation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String corporationName;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String primaryContactPersonFirstname;
	private String primaryContactPersonLastname;
	private String emailAddrForInvoice;
	private String paymentType;
	private String phoneNumber;
	private String faxNumber;
	private String cardNumber;
	private String expirationDate;
	private String cardCode;
	private FastRxAudit fastRxAudit;
	private String active;

	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCorporationName() {
		return corporationName;
	}
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
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
	public String getPrimaryContactPersonFirstname() {
		return primaryContactPersonFirstname;
	}
	public void setPrimaryContactPersonFirstname(String primaryContactPersonFirstname) {
		this.primaryContactPersonFirstname = primaryContactPersonFirstname;
	}
	public String getPrimaryContactPersonLastname() {
		return primaryContactPersonLastname;
	}
	public void setPrimaryContactPersonLastname(String primaryContactPersonLastname) {
		this.primaryContactPersonLastname = primaryContactPersonLastname;
	}
	public String getEmailAddrForInvoice() {
		return emailAddrForInvoice;
	}
	public void setEmailAddrForInvoice(String emailAddrForInvoice) {
		this.emailAddrForInvoice = emailAddrForInvoice;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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
	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}
	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	

}
