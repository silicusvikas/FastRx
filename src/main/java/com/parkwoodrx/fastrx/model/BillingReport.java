package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class BillingReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private double totalRequestingCharge;
//	private double totalrespondingCharge;
	private double totalRequestingTax;
//	private double totalrespondingtax;
//	private double totalEfaxRespondingCharge;
	
	public double getTotalRequestingCharge() {
		return totalRequestingCharge;
	}
	public void setTotalRequestingCharge(double totalRequestingCharge) {
		this.totalRequestingCharge = totalRequestingCharge;
	}

	public double getTotalRequestingTax() {
		return totalRequestingTax;
	}
	public void setTotalRequestingTax(double totalRequestingTax) {
		this.totalRequestingTax = totalRequestingTax;
	}
	
	
}
