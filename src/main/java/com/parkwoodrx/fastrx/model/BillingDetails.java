package com.parkwoodrx.fastrx.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class BillingDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private long corporationId;
	private FastRxAudit fastRxAudit;
	private double taxPerMonth;
	private double taxPerDay;
	private double transactionRatePerDay;
	private double transactionRatePerMonth;
	private String transactionType;
	private Timestamp transactionTypeChangeLog;
	private Timestamp start_Day;
	private String start_txnType;
	
	
	public String getStart_txnType() {
		return start_txnType;
	}
	public void setStart_txnType(String start_txnType) {
		this.start_txnType = start_txnType;
	}
	public Timestamp getStart_Day() {
		return start_Day;
	}
	public void setStart_Day(Timestamp start_Day) {
		this.start_Day = start_Day;
	}
	public Timestamp getTransactionTypeChangeLog() {
		return transactionTypeChangeLog;
	}
	public void setTransactionTypeChangeLog(Timestamp transactionTypeChangeLog) {
		this.transactionTypeChangeLog = transactionTypeChangeLog;
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
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}
	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
