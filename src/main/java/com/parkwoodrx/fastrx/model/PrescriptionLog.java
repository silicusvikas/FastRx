package com.parkwoodrx.fastrx.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author PPaswan
 *
 */
public class PrescriptionLog implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;
	private Timestamp date;
	private String patientFirstName;
	private String patientLastName;
	private String patientDob;
	private String patientAddress;
	private String prescNumber;
	private String prescDrugName;
	private Event event;
	private String remarks;
	private String updatedBy;
	private Status status;
	
	public enum Event {
		 TRANSFER_OUT , TRANSFER_IN
	};
	public enum Status {
		SUCCESS, FAIL
	};
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
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
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
