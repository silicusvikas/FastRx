package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;

public class NonRegPharMapper implements RowMapper<NonRegPharmacy>  {

	@Override
	public NonRegPharmacy mapRow(ResultSet result, int rowNum) throws SQLException {

		NonRegPharmacy nonRegPharmacy = new NonRegPharmacy();			
			nonRegPharmacy.setId(result.getLong("id"));		
			nonRegPharmacy.setPharmacy_name(result.getString("pharmacy_name"));
			nonRegPharmacy.setNpi(result.getString("npi_num"));
			nonRegPharmacy.setDea(result.getString("dea_num"));		
			nonRegPharmacy.setStore_address(result.getString("address"));		
			nonRegPharmacy.setPhy_city(result.getString("city"));
			nonRegPharmacy.setPhy_state(result.getString("state"));
			nonRegPharmacy.setPhy_zip(result.getString("zip_code"));
			nonRegPharmacy.setPhy_phone(result.getString("phone_num"));
			nonRegPharmacy.setPhy_efax(result.getString("fax_num"));
			nonRegPharmacy.setStore_number(result.getString("store_number"));
			return nonRegPharmacy;
		}
	
}
