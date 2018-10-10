package com.parkwoodrx.fastrx.model;

import java.io.Serializable;

public class RespondPrescription implements Serializable {
	private static final long serialVersionUID = 1L;

	private Prescription prescription;
	private PharmacyLocation requestingPharmacy;
	private NonRegPharmacy respondingPharmacy;
	private PharmacyCorporation requestingCorp;
	private PharmacyCorporation respondingCorp;
	private User requestingUser;
	private User respondingUser;

	public Prescription getPrescription() {
		return prescription;
	}

	public void setPrescription(Prescription prescription) {
		this.prescription = prescription;
	}

	public PharmacyLocation getRequestingPharmacy() {
		return requestingPharmacy;
	}

	public void setRequestingPharmacy(PharmacyLocation requestingPharmacy) {
		this.requestingPharmacy = requestingPharmacy;
	}

	
	public NonRegPharmacy getRespondingPharmacy() {
		return respondingPharmacy;
	}

	public void setRespondingPharmacy(NonRegPharmacy respondingPharmacy) {
		this.respondingPharmacy = respondingPharmacy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public User getRequestingUser() {
		return requestingUser;
	}

	public void setRequestingUser(User requestingUser) {
		this.requestingUser = requestingUser;
	}

	public User getRespondingUser() {
		return respondingUser;
	}

	public void setRespondingUser(User respondingUser) {
		this.respondingUser = respondingUser;
	}

}
