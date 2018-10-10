package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.UserList;


public class UserListRowMapper implements RowMapper<UserList>  {

	@Override
	public UserList mapRow(ResultSet result, int rowNum) throws SQLException {
		UserList userList=new UserList();
		FastRxAudit fastRxAudit=new FastRxAudit();
		userList.setId(result.getInt("ID"));
		userList.setRoleId(result.getInt("ROLE_ID"));
		userList.setFirstName(result.getString("FIRST_NAME"));
		userList.setLastName(result.getString("LAST_NAME"));
		userList.setUsername(result.getString("USERNAME"));
		userList.setPassword(result.getString("PASSWORD"));
		userList.setPhoneNumber(result.getString("PHONE_NUM"));
		userList.setPharmacistLicenseNumber(result.getString("PHARMACIST_LICENSE_NUM"));
		userList.setStateLicenseNumber(result.getString("STATE_LICENSE_NUM"));
		userList.setActive(result.getString("ACTIVE"));
		fastRxAudit.setCreatedOn(result.getTimestamp("created_on"));
		fastRxAudit.setCreatedBy(result.getString("created_by"));
		fastRxAudit.setUpdatedOn(result.getTimestamp("updated_on"));
		fastRxAudit.setUpdatedBy(result.getString("updated_by"));
		userList.setFastRxAudit(fastRxAudit);
		userList.setCorporationName(result.getString("corporation_name"));
		return userList;
	}

}
