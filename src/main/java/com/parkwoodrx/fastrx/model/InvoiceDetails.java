package com.parkwoodrx.fastrx.model;

import java.security.Timestamp;

public class InvoiceDetails {
	
	private long id;
	private long corporationId;
	private double billingAmount;
	private Timestamp invoiceDate;
	private String billingPeriod;
	private String txnRefId;
	private String status;
	private String reason;
	private FastRxAudit fastRxAudit;
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
	public double getBillingAmount() {
		return billingAmount;
	}
	public void setBillingAmount(double billingAmount) {
		this.billingAmount = billingAmount;
	}
	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public String getBillingPeriod() {
		return billingPeriod;
	}
	public void setBillingPeriod(String billingPeriod) {
		this.billingPeriod = billingPeriod;
	}
	public String getTxnRefId() {
		return txnRefId;
	}
	public void setTxnRefId(String txnRefId) {
		this.txnRefId = txnRefId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}
	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}
	@Override
	public String toString() {
		return "InvoiceDetails [id=" + id + ", corporationId=" + corporationId + ", billingAmount=" + billingAmount
				+ ", invoiceDate=" + invoiceDate + ", billingPeriod=" + billingPeriod + ", txnRefId=" + txnRefId
				+ ", status=" + status + ", reason=" + reason + ", fastRxAudit=" + fastRxAudit + "]";
	}

}
