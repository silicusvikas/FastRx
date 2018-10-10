package com.parkwoodrx.fastrx.model;

import java.sql.Timestamp;

public class TransactionTable {
	private String corporationName;
	private String pharmacyName;
	private Timestamp createdOn;
	private String transactionType;
	private double taxPerMonth;
	private double taxPerDay;
	private double transactionRatePerDay;
	private double transactionRatePerMonth;
	private String requestingCity;
	private String requestingState;
	private String requestingZip;
	private String respondingCity;
	private String respondingState;
	private String respondingZip;
	private String drugName;
	private int drugQuantity;
	private int refillsRemaining;
	private String patientDoB;
	private String paymentType;	

	
	public String getCorporationName() {
		return corporationName;
	}
	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName;
	}
	public String getPharmacyName() {
		return pharmacyName;
	}
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public double getTaxPerMonth() {
		return taxPerMonth;
	}
	public void setTaxPerMonth(double taxPerMonth) {
		this.taxPerMonth = taxPerMonth;
	}
	public double getTaxPerDay() {
		return taxPerDay;
	}
	public void setTaxPerDay(double taxPerDay) {
		this.taxPerDay = taxPerDay;
	}
	public double getTransactionRatePerDay() {
		return transactionRatePerDay;
	}
	public void setTransactionRatePerDay(double transactionRatePerDay) {
		this.transactionRatePerDay = transactionRatePerDay;
	}
	public double getTransactionRatePerMonth() {
		return transactionRatePerMonth;
	}
	public void setTransactionRatePerMonth(double transactionRatePerMonth) {
		this.transactionRatePerMonth = transactionRatePerMonth;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getRequestingCity() {
		return requestingCity;
	}
	public void setRequestingCity(String requestingCity) {
		this.requestingCity = requestingCity;
	}
	public String getRequestingState() {
		return requestingState;
	}
	public void setRequestingState(String requestingState) {
		this.requestingState = requestingState;
	}
	public String getRequestingZip() {
		return requestingZip;
	}
	public void setRequestingZip(String requestingZip) {
		this.requestingZip = requestingZip;
	}
	public String getRespondingCity() {
		return respondingCity;
	}
	public void setRespondingCity(String respondingCity) {
		this.respondingCity = respondingCity;
	}
	public String getRespondingState() {
		return respondingState;
	}
	public void setRespondingState(String respondingState) {
		this.respondingState = respondingState;
	}
	public String getRespondingZip() {
		return respondingZip;
	}
	public void setRespondingZip(String respondingZip) {
		this.respondingZip = respondingZip;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public int getDrugQuantity() {
		return drugQuantity;
	}
	public void setDrugQuantity(int drugQuantity) {
		this.drugQuantity = drugQuantity;
	}
	public int getRefillsRemaining() {
		return refillsRemaining;
	}
	public void setRefillsRemaining(int refillsRemaining) {
		this.refillsRemaining = refillsRemaining;
	}
	public String getPatientDoB() {
		return patientDoB;
	}
	public void setPatientDoB(String patientDoB) {
		this.patientDoB = patientDoB;
	}	
}
