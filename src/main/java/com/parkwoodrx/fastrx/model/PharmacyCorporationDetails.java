package com.parkwoodrx.fastrx.model;

public class PharmacyCorporationDetails implements Cloneable {
	
	private long id;
	private User userDetails;
	private PharmacyCorporation pharmacyCorporation;
	private BillingDetails billingDetails;
	private PaymentProfileDetails paymentProfileDetails;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	public PharmacyCorporation getPharmacyCorporation() {
		return pharmacyCorporation;
	}

	public void setPharmacyCorporation(PharmacyCorporation pharmacyCorporation) {
		this.pharmacyCorporation = pharmacyCorporation;
	}

	public BillingDetails getBillingDetails() {
		return billingDetails;
	}

	public void setBillingDetails(BillingDetails billingDetails) {
		this.billingDetails = billingDetails;
	}

	public PaymentProfileDetails getPaymentProfileDetails() {
		return paymentProfileDetails;
	}

	public void setPaymentProfileDetails(PaymentProfileDetails paymentProfileDetails) {
		this.paymentProfileDetails = paymentProfileDetails;
	}

	
	
	

}
