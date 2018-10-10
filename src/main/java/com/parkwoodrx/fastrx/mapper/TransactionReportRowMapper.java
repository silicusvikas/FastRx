package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionTransactionDetails;
import com.parkwoodrx.fastrx.model.TransactionReport;
public class TransactionReportRowMapper implements RowMapper<TransactionReport>{

	@Override
	public TransactionReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		TransactionReport transactionReport = new TransactionReport();		
		PrescriptionTransactionDetails transaction= new PrescriptionTransactionDetails();
		PharmacyLocation requestingPharmacy = new PharmacyLocation();
		PharmacyLocation respondingPharmacy = new PharmacyLocation();
		PharmacyCorporation requestingCorp = new PharmacyCorporation();
		PharmacyCorporation respondingCorp = new PharmacyCorporation();
		Prescription prescription= new Prescription();
		FastRxAudit fastRxAudit = new FastRxAudit();
				
		prescription.setPatientDob(rs.getString("patient_dob"));
		prescription.setPrescDrugName(rs.getString("presc_drug_name"));
		prescription.setPrescDrugQty(rs.getInt("presc_drug_qty"));
		prescription.setRefillsRemaining(rs.getInt("refills_remaining"));
		transaction.setId(rs.getLong("id"));
		transaction.setReqPharmacyId(rs.getLong("req_corporation_id"));
		//transaction.setReqPharmacyId(rs.getLong("res_corporation_id"));
		transaction.setReqPharmacyId(rs.getLong("req_pharmacy_id"));
		transaction.setResPharmacyId(rs.getLong("res_pharmacy_id"));
		
		transaction.setTaxPerDay(rs.getDouble("tax_per_day"));
		transaction.setTaxPerMonth(rs.getDouble("tax_per_month"));		
		transaction.setTransactionRatePerDay(rs.getDouble("transaction_rate_per_day"));
		transaction.setTransactionRatePerMonth(rs.getDouble("transaction_rate_per_month"));
		transaction.setTransactionType(rs.getString("transaction_type"));
		
		transaction.setTxnStatus(rs.getString("txn_status"));
		transaction.setPrescription_id(rs.getLong("prescription_id"));
		
		requestingPharmacy.setPharmacyName(rs.getString("reqP"));
		requestingPharmacy.setCity(rs.getString("reqc"));
		requestingPharmacy.setState(rs.getString("reqs"));
		requestingPharmacy.setZipcode(rs.getString("reqz"));
		respondingPharmacy.setPharmacyName(rs.getString("resP"));
		respondingPharmacy.setCity(rs.getString("resc"));
		respondingPharmacy.setState(rs.getString("ress"));
		respondingPharmacy.setZipcode(rs.getString("resz"));
		
		requestingCorp.setCorporationName(rs.getString("reqCorpName"));
		requestingCorp.setPaymentType(rs.getString("reqPayType"));
		
	//	respondingCorp.setCorporationName(rs.getString("resCorpName"));
	//	respondingCorp.setPaymentType(rs.getString("resPayType"));

		fastRxAudit.setCreatedBy(rs.getString("created_by"));
		fastRxAudit.setCreatedOn(rs.getTimestamp("created_on"));
		fastRxAudit.setUpdatedBy(rs.getString("updated_by"));
		fastRxAudit.setUpdatedOn(rs.getTimestamp("updated_on"));
		transaction.setFastRxAudit(fastRxAudit);
		
		transactionReport.setTransaction(transaction);
		transactionReport.setRequestingPharmacy(requestingPharmacy);
		transactionReport.setRespondingPharmacy(respondingPharmacy);
		transactionReport.setRequestingCorp(requestingCorp);
		transactionReport.setRespondingCorp(respondingCorp);
		//transactionReport.setTransactionType(rs.getString("TxType"));
		transactionReport.setPrescription(prescription);
		
		return transactionReport;
	}

}
