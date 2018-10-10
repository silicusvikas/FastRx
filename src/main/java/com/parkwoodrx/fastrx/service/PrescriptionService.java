package com.parkwoodrx.fastrx.service;

import java.util.List;
import java.util.Map;

import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.model.RespondPrescription;

public interface PrescriptionService {
	public void batchInsertPrescriptionReqs(List<Prescription> prescriptions);

	public List<Prescription> getListOfPrescriptionsForLocation(long locationId);

	public RespondPrescription getPrescriptionDetailsById(long prescriptionId);

	public void updateSendFaxStatus(String prescriptionId);

	public void respondPrescription(Prescription prescription);

	public List<PrescriptionLog> getPrescriptionLogs();

	public List<PrescriptionLog> searchPrescriptionLogByNameAndDate(String username, String startDate, String endDate);

	public void updateFaxStatusByFaxId(String updatedBy, Map<String, Integer> faxStatusDetail);
	
	public void resendPrescription(RespondPrescription prescription);

}
