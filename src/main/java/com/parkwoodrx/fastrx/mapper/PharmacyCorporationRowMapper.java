package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;

public class PharmacyCorporationRowMapper implements RowMapper<PharmacyCorporation>  {

	@Override
	public PharmacyCorporation mapRow(ResultSet rs, int rowNum) throws SQLException {
		PharmacyCorporation pharmacyCorporation=new PharmacyCorporation();
		FastRxAudit fastRxAudit=new FastRxAudit();
		pharmacyCorporation.setId(rs.getLong("id"));
		pharmacyCorporation.setCorporationName(rs.getString("corporation_name"));
		pharmacyCorporation.setAddress(rs.getString("address"));
		pharmacyCorporation.setCity(rs.getString("city"));
		pharmacyCorporation.setState(rs.getString("state"));
		pharmacyCorporation.setZipCode(rs.getString("zip_code"));
		pharmacyCorporation.setPrimaryContactPersonFirstname(rs.getString("primary_contact_person_firstname"));
		pharmacyCorporation.setPrimaryContactPersonLastname(rs.getString("primary_contact_person_lastname"));
		pharmacyCorporation.setEmailAddrForInvoice(rs.getString("email_addr_for_invoice"));
		pharmacyCorporation.setPaymentType(rs.getString("payment_type"));
		pharmacyCorporation.setPhoneNumber(rs.getString("phone_num"));
		pharmacyCorporation.setFaxNumber(rs.getString("fax_number"));
		fastRxAudit.setCreatedOn(rs.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(rs.getString("created_by"));
		fastRxAudit.setUpdatedOn(rs.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(rs.getString("updated_by"));
		pharmacyCorporation.setFastRxAudit(fastRxAudit);
		pharmacyCorporation.setActive(rs.getString("active"));
		return pharmacyCorporation;
		
	}

}
