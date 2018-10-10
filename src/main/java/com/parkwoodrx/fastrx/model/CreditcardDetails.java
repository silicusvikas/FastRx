package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class CreditcardDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long corporationId;
	private String customerProfileId;
	private String customerPaymentAcctId;
	private String activeProfile;
	private String response;
	private FastRxAudit fastRxAudit;
	private String cardHolderName;
	private String cardNumber;
	
	
	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}

	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
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

	public String getCustomerProfileId() {
		return customerProfileId;
	}

	public void setCustomerProfileId(String customerProfileId) {
		this.customerProfileId = customerProfileId;
	}

	public String getCustomerPaymentAcctId() {
		return customerPaymentAcctId;
	}

	public void setCustomerPaymentAcctId(String customerPaymentAcctId) {
		this.customerPaymentAcctId = customerPaymentAcctId;
	}

	public String getActiveProfile() {
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}

}
