package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class RevenueReport implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long corpId;
	private String corpName;
	private double txnCharge;
	private double taxCharge;
	
	public long getCorpId() {
		return corpId;
	}
	public void setCorpId(long corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public double getTxnCharge() {
		return txnCharge;
	}
	public void setTxnCharge(double txnCharge) {
		this.txnCharge = txnCharge;
	}
	public double getTaxCharge() {
		return taxCharge;
	}
	public void setTaxCharge(double taxCharge) {
		this.taxCharge = taxCharge;
	}
	
}
