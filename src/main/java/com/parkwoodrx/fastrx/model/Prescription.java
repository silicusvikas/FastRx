package com.parkwoodrx.fastrx.model;

import java.io.Serializable;
import java.sql.Date;

public class Prescription implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private long requestingCorpId;
	private long respondingCorpId;
	private long requestingPharmacyId;
	private long respondingPharmacyId;
	private String patientFirstName;
	private String patientLastName;
	private String patientDob;
	private String patientAddress;
	private String respTime;
	private String prescNumber;
	private String prescDrugName;
	private int prescDrugQty;
	private String directions;
	private Date origdateWritten;
	private Date dateLastFilled;
	private Date originalfilldate;
	private int refillsRemaining;
	private String providerName;
	private String providerPhoneNumber;
	private String providerNpi;
	private String providerDea;
	private String reqPharmacyComments;
	private String resPharmacyComments;
	private String status;
	private String efaxSent;
	private String reqToken;
	private FastRxAudit fastRxAudit;
	private String resendFlag;	
	private String iscompound;
	private String pharmacist;
	private String faxJobId;
	private String faxStatus;
	
	public String getFaxJobId() {
		return faxJobId;
	}
	public void setFaxJobId(String faxJobId) {
		this.faxJobId = faxJobId;
	}
	
	public String getFaxStatus() {
		return faxStatus;
	}
	public void setFaxStatus(String faxStatus) {
		this.faxStatus = faxStatus;
	}
	public String getPharmacist() {
		return pharmacist;
	}
	public void setPharmacist(String pharmacist) {
		this.pharmacist = pharmacist;
	}
	public String getIscompound() {
		return iscompound;
	}
	public void setIscompound(String iscompound) {
		this.iscompound = iscompound;
	}

	private String respondTime;
	
	public String getResendFlag() {
		return resendFlag;
	}
	public void setResendFlag(String resendFlag) {
		this.resendFlag = resendFlag;
	}
	
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCompoundCheckFlag() {
		return iscompound;
	}
	public void setCompoundCheckFlag(String iscompound) {
		this.iscompound = iscompound;
	}
	public String getRespondTime() {
		return respondTime;
	}
	public void setRespondTime(String respondTime) {
		this.respondTime = respondTime;
	}
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

	public long getRequestingPharmacyId() {
		return requestingPharmacyId;
	}

	public void setRequestingPharmacyId(long requestingPharmacyId) {
		this.requestingPharmacyId = requestingPharmacyId;
	}

	public long getRespondingPharmacyId() {
		return respondingPharmacyId;
	}

	public void setRespondingPharmacyId(long respondingPharmacyId) {
		this.respondingPharmacyId = respondingPharmacyId;
	}

	

	public String getPatientFirstName() {
		return patientFirstName;
	}

	public void setPatientFirstName(String patientFirstName) {
		this.patientFirstName = patientFirstName;
	}

	public String getPatientLastName() {
		return patientLastName;
	}

	public void setPatientLastName(String patientLastName) {
		this.patientLastName = patientLastName;
	}

	public String getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(String patientDob) {
		this.patientDob = patientDob;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public String getPrescNumber() {
		return prescNumber;
	}

	public void setPrescNumber(String prescNumber) {
		this.prescNumber = prescNumber;
	}

	public String getPrescDrugName() {
		return prescDrugName;
	}

	public void setPrescDrugName(String prescDrugName) {
		this.prescDrugName = prescDrugName;
	}

	public int getPrescDrugQty() {
		return prescDrugQty;
	}

	public void setPrescDrugQty(int prescDrugQty) {
		this.prescDrugQty = prescDrugQty;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public Date getOrigdateWritten() {
		return origdateWritten;
	}

	public void setOrigdateWritten(Date origdateWritten) {
		this.origdateWritten = origdateWritten;
	}

	public Date getDateLastFilled() {
		return dateLastFilled;
	}

	public void setDateLastFilled(Date dateLastFilled) {
		this.dateLastFilled = dateLastFilled;
	}

	public int getRefillsRemaining() {
		return refillsRemaining;
	}

	public void setRefillsRemaining(int refillsRemaining) {
		this.refillsRemaining = refillsRemaining;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getProviderPhoneNumber() {
		return providerPhoneNumber;
	}

	public void setProviderPhoneNumber(String providerPhoneNumber) {
		this.providerPhoneNumber = providerPhoneNumber;
	}

	public String getProviderNpi() {
		return providerNpi;
	}

	public void setProviderNpi(String providerNpi) {
		this.providerNpi = providerNpi;
	}

	public String getProviderDea() {
		return providerDea;
	}

	public void setProviderDea(String providerDea) {
		this.providerDea = providerDea;
	}

	public String getReqPharmacyComments() {
		return reqPharmacyComments;
	}

	public void setReqPharmacyComments(String reqPharmacyComments) {
		this.reqPharmacyComments = reqPharmacyComments;
	}

	public String getResPharmacyComments() {
		return resPharmacyComments;
	}

	public void setResPharmacyComments(String resPharmacyComments) {
		this.resPharmacyComments = resPharmacyComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public FastRxAudit getFastRxAudit() {
		return fastRxAudit;
	}

	public void setFastRxAudit(FastRxAudit fastRxAudit) {
		this.fastRxAudit = fastRxAudit;
	}

	public String getEfaxSent() {
		return efaxSent;
	}

	public void setEfaxSent(String efaxSent) {
		this.efaxSent = efaxSent;
	}

	public String getReqToken() {
		return reqToken;
	}

	public void setReqToken(String reqToken) {
		this.reqToken = reqToken;
	}

	public Date getOriginalfilldate() {
		return originalfilldate;
	}

	public void setOriginalfilldate(Date originalfilldate) {
		this.originalfilldate = originalfilldate;
	}
	

}
