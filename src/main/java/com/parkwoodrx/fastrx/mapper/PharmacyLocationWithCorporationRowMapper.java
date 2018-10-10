package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;

public class PharmacyLocationWithCorporationRowMapper implements RowMapper<PharmacyLocationWithCorporation> {
	@Override
	public PharmacyLocationWithCorporation mapRow(ResultSet result, int rownum) throws SQLException {
		PharmacyLocationWithCorporation pharmacyLocation = new PharmacyLocationWithCorporation();
		FastRxAudit fastRxAudit=new FastRxAudit();
		pharmacyLocation.setId(result.getLong("ID"));
		pharmacyLocation.setCorporationId(result.getLong("CORPORATION_ID"));
		pharmacyLocation.setPharmacyName(result.getString("PHARMACY_NAME"));
		pharmacyLocation.setStateLicenseNumber(result.getString("STATE_LICENSE_NUM"));
		pharmacyLocation.setNcpdpNumber(result.getString("NCPDP_NUM"));
		pharmacyLocation.setNpiNumber(result.getString("NPI_NUM"));
		pharmacyLocation.setDeaNumber(result.getString("DEA_NUM"));
		pharmacyLocation.setLocationPin(result.getString("LOCATION_PIN"));
		pharmacyLocation.setAddress(result.getString("ADDRESS"));
		pharmacyLocation.setActive(result.getString("ACTIVE"));
		pharmacyLocation.setCity(result.getString("CITY"));
		pharmacyLocation.setState(result.getString("STATE"));
		pharmacyLocation.setZipcode(result.getString("ZIP_CODE"));
		pharmacyLocation.setPhoneNumber(result.getString("PHONE_NUM"));
		pharmacyLocation.setFaxNumber(result.getString("FAX_NUM"));
		pharmacyLocation.setPharmacyStoreNumber(result.getString("pharmacy_store_num"));
		pharmacyLocation.setCorporationName(result.getString("corporation_name"));
		fastRxAudit.setCreatedOn(result.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(result.getString("created_by"));
		fastRxAudit.setUpdatedOn(result.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(result.getString("updated_by"));
		pharmacyLocation.setFastRxAudit(fastRxAudit);
		return pharmacyLocation;
	}

}
