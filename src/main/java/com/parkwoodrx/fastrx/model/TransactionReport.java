package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class TransactionReport implements Serializable {
	private static final long serialVersionUID = 1L;

	private PrescriptionTransactionDetails transaction;
	private PharmacyLocation requestingPharmacy;
	private PharmacyLocation respondingPharmacy;
	private PharmacyCorporation requestingCorp;
	private PharmacyCorporation respondingCorp;
	private String transactionType;
	private Prescription prescription;	
	public PrescriptionTransactionDetails getTransaction() {
		return transaction;
	}
	public void setTransaction(PrescriptionTransactionDetails transaction) {
		this.transaction = transaction;
	}

	public PharmacyLocation getRequestingPharmacy() {
		return requestingPharmacy;
	}

	public void setRequestingPharmacy(PharmacyLocation requestingPharmacy) {
		this.requestingPharmacy = requestingPharmacy;
	}

	public PharmacyLocation getRespondingPharmacy() {
		return respondingPharmacy;
	}

	public void setRespondingPharmacy(PharmacyLocation respondingPharmacy) {
		this.respondingPharmacy = respondingPharmacy;
	}

	public PharmacyCorporation getRequestingCorp() {
		return requestingCorp;
	}

	public void setRequestingCorp(PharmacyCorporation requestingCorp) {
		this.requestingCorp = requestingCorp;
	}

	public PharmacyCorporation getRespondingCorp() {
		return respondingCorp;
	}

	public void setRespondingCorp(PharmacyCorporation respondingCorp) {
		this.respondingCorp = respondingCorp;
	}

	public String getTransactionType() {
		return transactionType;
	}

	
	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

}
