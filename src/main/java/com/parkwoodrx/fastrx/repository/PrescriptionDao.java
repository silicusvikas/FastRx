package com.parkwoodrx.fastrx.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.RespondPrescription;

public interface PrescriptionDao {
	public void batchInsertPrescriptionReqs(List<Prescription> prescriptions, String reqToken, long reqCorpId,
			long resCorpId) throws DataAccessException;

	public List<Prescription> getListOfPrescriptionsForLocation(long locationId);

	public RespondPrescription getPrescriptionDetailsById(long prescriptionId);

	public List<Prescription> getPrescriptionListForReqToken(String reqToken);

	public void respondPrescription(Prescription prescription);

	public void addFaxDetails(String faxJobId, String faxStatus, String updatedBy, long prescriptionId);

	public void updateFaxStatusByFaxId(String updatedBy, String faxStatus, String faxJobId);

}
