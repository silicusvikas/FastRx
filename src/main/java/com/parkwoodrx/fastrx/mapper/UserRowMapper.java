package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.User;

public class UserRowMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet result, int rowNum) throws SQLException {
		User user = new User();
		FastRxAudit fastRxAudit=new FastRxAudit();
		user.setId(result.getInt("ID"));
		user.setRoleId(result.getInt("ROLE_ID"));
		user.setFirstName(result.getString("FIRST_NAME"));
		user.setLastName(result.getString("LAST_NAME"));
		user.setUsername(result.getString("USERNAME"));
		user.setPassword(result.getString("PASSWORD"));
		user.setPhoneNumber(result.getString("PHONE_NUM"));
		user.setEmail(result.getString("EMAIL"));
		user.setPharmacistLicenseNumber(result.getString("PHARMACIST_LICENSE_NUM"));
		user.setStateLicenseNumber(result.getString("STATE_LICENSE_NUM"));
		user.setActive(result.getString("ACTIVE"));
		user.setFirstLogin(result.getTimestamp("first_login"));
		fastRxAudit.setCreatedOn(result.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(result.getString("created_by"));
		fastRxAudit.setUpdatedOn(result.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(result.getString("updated_by"));
		user.setFastRxAudit(fastRxAudit);
        return user;
	}

}
