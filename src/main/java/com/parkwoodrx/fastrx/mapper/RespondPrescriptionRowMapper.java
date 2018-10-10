package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.User;

public class RespondPrescriptionRowMapper implements RowMapper<RespondPrescription>{

	@Override
	public RespondPrescription mapRow(ResultSet rs, int rowNum) throws SQLException {
		RespondPrescription respondPrescription = new RespondPrescription();
		PharmacyLocation requestingPharmacy = new PharmacyLocation();
		NonRegPharmacy respondingPharmacy = new NonRegPharmacy();
		PharmacyCorporation requestingCorp = new PharmacyCorporation();
		PharmacyCorporation respondingCorp = new PharmacyCorporation();
		User requestingUser= new User();
		User respondingUser= new User();
		
		Prescription prescription = new Prescription();
		FastRxAudit fastRxAudit = new FastRxAudit();
		
		requestingPharmacy.setPharmacyName(rs.getString("reqP"));
		requestingPharmacy.setAddress(rs.getString("reqA"));
		requestingPharmacy.setCity(rs.getString("reqC"));
		requestingPharmacy.setState(rs.getString("reqS"));
		requestingPharmacy.setZipcode(rs.getString("reqZ"));
		requestingPharmacy.setDeaNumber(rs.getString("reqD"));	
		requestingPharmacy.setFaxNumber(rs.getString("reqFN"));
		requestingPharmacy.setPhoneNumber(rs.getString("reqPH"));
		requestingCorp.setCorporationName(rs.getString("reqCorpName"));		
		requestingUser.setFirstName(rs.getString("reqUserFname"));
		requestingUser.setLastName(rs.getString("reqUserLname"));		
		respondingPharmacy.setPharmacy_name(rs.getString("resP"));
		respondingPharmacy.setStore_address(rs.getString("resA"));
		respondingPharmacy.setPhy_city(rs.getString("resC"));
		respondingPharmacy.setPhy_state(rs.getString("resS"));
		respondingPharmacy.setPhy_zip(rs.getString("resZ"));
		respondingPharmacy.setDea(rs.getString("resD"));
		respondingPharmacy.setPhy_efax(rs.getString("resFN"));
		respondingPharmacy.setPhy_phone(rs.getString("resPH"));
		respondingPharmacy.setId(rs.getLong("resId"));
		respondingPharmacy.setStore_number(rs.getString("resSN"));
		prescription.setId(rs.getLong("id"));
		prescription.setRespTime(rs.getString("respTime"));
		prescription.setRequestingCorpId(rs.getLong("req_corporation_id"));
		prescription.setRequestingPharmacyId(rs.getLong("req_pharmacy_id"));
		prescription.setRespondingPharmacyId(rs.getLong("res_pharmacy_id"));
		prescription.setPatientFirstName(rs.getString("patient_firstname"));
		prescription.setPatientLastName(rs.getString("patient_lastname"));
		prescription.setPatientAddress(rs.getString("patient_address"));
		prescription.setPatientDob(rs.getString("patient_dob"));
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
		fastRxAudit.setCreatedBy(rs.getString("created_by"));
		fastRxAudit.setCreatedOn(rs.getTimestamp("created_on"));
		fastRxAudit.setUpdatedBy(rs.getString("updated_by"));
		fastRxAudit.setUpdatedOn(rs.getTimestamp("updated_on"));
		prescription.setFastRxAudit(fastRxAudit);
		prescription.setOriginalfilldate(rs.getDate("original_fill_date"));		
		respondPrescription.setPrescription(prescription);
		respondPrescription.setRequestingPharmacy(requestingPharmacy);
		respondPrescription.setRespondingPharmacy(respondingPharmacy);
		respondPrescription.setRequestingCorp(requestingCorp);
		respondPrescription.setRespondingCorp(respondingCorp);
		respondPrescription.setRequestingUser(requestingUser);
		respondPrescription.setRespondingUser(respondingUser);
		
		return respondPrescription;
	}

}
