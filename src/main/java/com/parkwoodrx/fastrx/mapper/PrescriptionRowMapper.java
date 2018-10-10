package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.Prescription;

public class PrescriptionRowMapper implements RowMapper<Prescription> {

	@Override
	public Prescription mapRow(ResultSet rs, int rowNum) throws SQLException {
		Prescription prescription = new Prescription();
		FastRxAudit fastRxAudit = new FastRxAudit();
		prescription.setId(rs.getLong("id"));
		prescription.setRequestingCorpId(rs.getLong("req_corporation_id"));
		prescription.setRespondingCorpId(rs.getLong("res_corporation_id"));
		prescription.setRequestingPharmacyId(rs.getLong("req_pharmacy_id"));
		prescription.setRespondingPharmacyId(rs.getLong("res_pharmacy_id"));
		prescription.setPatientFirstName(rs.getString("patient_firstname"));
		prescription.setPatientLastName(rs.getString("patient_lastname"));
		prescription.setPatientDob(rs.getString("patient_dob"));
		prescription.setPatientAddress(rs.getString("patient_address"));
		prescription.setResendFlag(rs.getString("resendFlag"));
		prescription.setRespTime(rs.getString("respTime"));
		prescription.setPrescNumber(rs.getString("presc_number"));
		prescription.setPrescDrugName(rs.getString("presc_drug_name"));
		prescription.setPrescDrugQty(rs.getInt("presc_drug_qty"));
		prescription.setDirections(rs.getString("directions"));
		prescription.setOrigdateWritten(rs.getDate("orig_date_written"));
		prescription.setDateLastFilled(rs.getDate("date_last_filled"));
		prescription.setRefillsRemaining(rs.getInt("refills_remaining"));
		prescription.setProviderName(rs.getString("provider_name"));
		prescription.setProviderPhoneNumber(rs.getString("provider_phone_number"));
		prescription.setProviderNpi(rs.getString("provider_npi"));
		prescription.setProviderDea(rs.getString("provider_dea"));
		prescription.setReqPharmacyComments(rs.getString("req_pharmacy_comments"));
		prescription.setResPharmacyComments(rs.getString("res_pharmacy_comments"));
		prescription.setStatus(rs.getString("status"));
		prescription.setEfaxSent(rs.getString("efax_sent"));
		prescription.setReqToken(rs.getString("req_token"));
		prescription.setFaxJobId(rs.getString("fax_job_id"));
		prescription.setFaxStatus(rs.getString("fax_status"));
		
		fastRxAudit.setCreatedOn(rs.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(rs.getString("created_by"));
		fastRxAudit.setUpdatedOn(rs.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(rs.getString("updated_by"));
		
		prescription.setFastRxAudit(fastRxAudit);
		return prescription;
	}

}
