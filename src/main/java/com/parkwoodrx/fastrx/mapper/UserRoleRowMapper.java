package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.UserRole;

public class UserRoleRowMapper  implements RowMapper<UserRole>{

	@Override
	public UserRole mapRow(ResultSet result, int rowNum) throws SQLException {
		UserRole userRole=new UserRole();
		userRole.setId(result.getLong("ID"));
		userRole.setRoleName(result.getString("ROLE_NAME"));
		return userRole;
	}

}
