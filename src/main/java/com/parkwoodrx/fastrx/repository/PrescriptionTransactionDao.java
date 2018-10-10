package com.parkwoodrx.fastrx.repository;

import java.text.ParseException;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.model.UserLog;

public interface PrescriptionTransactionDao {
	public BillingDetails getBillingDetailsForCorpId(long corpId);
	public void batchInsertPrescriptionTransactions(List<Prescription> prescriptions, BillingDetails billingDetails,BillingDetails resbillingDetails);
	public void updatePrescriptionResponse(Prescription prescription, BillingDetails billingDetails);
	public void addPrescriptionLog(PrescriptionLog prescriptionLog);
	public void updateSendFaxStatus(String prescriptionId);
	public List<PrescriptionLog> getPrescriptionLogs();
	public void batchInsertPrescriptionLog(List<PrescriptionLog> prescriptionLogList);
	public List<PrescriptionLog> searchUserLogByNameAndDate(String name, String startDate, String endDate) throws DataAccessException, ParseException;
}
