package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.PrescriptionTransactionDetails;

public class TransactionDetailsRowMapper implements RowMapper<PrescriptionTransactionDetails>{

	@Override
	public PrescriptionTransactionDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrescriptionTransactionDetails prescriptionTransactionDetails= new PrescriptionTransactionDetails();
		FastRxAudit fastRxAudit=new FastRxAudit();
		
		prescriptionTransactionDetails.setId(rs.getLong("id"));
		prescriptionTransactionDetails.setRequestingCorpId(rs.getLong("req_corporation_id"));
		prescriptionTransactionDetails.setRespondingCorpId(rs.getLong("res_corporation_id"));
		prescriptionTransactionDetails.setReqPharmacyId(rs.getLong("req_pharmacy_id"));
		prescriptionTransactionDetails.setResPharmacyId(rs.getLong("res_pharmacy_id"));
		prescriptionTransactionDetails.setPrescription_id(rs.getLong("prescription_id"));
		prescriptionTransactionDetails.setTaxPerDay(rs.getDouble("tax_per_day"));
		prescriptionTransactionDetails.setTaxPerMonth(rs.getDouble("tax_per_month"));
		prescriptionTransactionDetails.setTransactionRatePerDay(rs.getDouble("transaction_rate_per_day"));
		prescriptionTransactionDetails.setTransactionRatePerMonth(rs.getDouble("transaction_rate_per_month"));
		prescriptionTransactionDetails.setTransactionType(rs.getString("transaction_type"));
		prescriptionTransactionDetails.setTxnStatus(rs.getString("txn_status"));		
		fastRxAudit.setCreatedOn(rs.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(rs.getString("created_by"));
		fastRxAudit.setUpdatedOn(rs.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(rs.getString("updated_by"));
		prescriptionTransactionDetails.setFastRxAudit(fastRxAudit);		
		return prescriptionTransactionDetails;
	}

}
