package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.User;

public class PharmacyCorporationDetailsRowMapper implements RowMapper<PharmacyCorporationDetails> {

	@Override
	public PharmacyCorporationDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		PharmacyCorporationDetails pharmacyCorporationDetails = new PharmacyCorporationDetails();
		PharmacyCorporation pharmacyCorporation = new PharmacyCorporation();
		User user = new User();
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
		pharmacyCorporation.setActive(rs.getString("active"));
		pharmacyCorporationDetails.setPharmacyCorporation(pharmacyCorporation);
		user.setUsername(rs.getString("username"));
		pharmacyCorporationDetails.setUserDetails(user);
		return pharmacyCorporationDetails;
	}

}
