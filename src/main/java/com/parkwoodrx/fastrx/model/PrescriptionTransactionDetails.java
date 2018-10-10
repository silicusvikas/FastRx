package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class PrescriptionTransactionDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id; 
	private long requestingCorpId;
	private long respondingCorpId;
	private long reqPharmacyId; 
	private long resPharmacyId; 
	private long prescription_id;
	private double taxPerMonth; 
	private double taxPerDay; 
	private double transactionRatePerDay;
	private double transactionRatePerMonth;
	private String transactionType;	
	private String txnStatus;
	private FastRxAudit fastRxAudit;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getRequestingCorpId() {
		return requestingCorpId;
	}
	public void setRequestingCorpId(long requestingCorpId) {
		this.requestingCorpId = requestingCorpId;
	}
	public long getRespondingCorpId() {
		return respondingCorpId;
	}
	public void setRespondingCorpId(long respondingCorpId) {
		this.respondingCorpId = respondingCorpId;
	}
	public long getReqPharmacyId() {
		return reqPharmacyId;
	}
	public void setReqPharmacyId(long reqPharmacyId) {
		this.reqPharmacyId = reqPharmacyId;
	}
	public long getResPharmacyId() {
		return resPharmacyId;
	}
	public void setResPharmacyId(long resPharmacyId) {
		this.resPharmacyId = resPharmacyId;
	}
	public long getPrescription_id() {
		return prescription_id;
	}
	public void setPrescription_id(long prescription_id) {
		this.prescription_id = prescription_id;
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getTxnStatus() {
		return txnStatus;
	}
	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}
	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}
	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	
	
	
	
}
